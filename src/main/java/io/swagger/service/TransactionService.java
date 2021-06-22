package io.swagger.service;

import io.swagger.model.*;
import io.swagger.model.DTO.DepositWithdrawalDTO;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.DTO.TransactionResponseDTO;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Transaction> getTransactionsByUser(Integer userId) {
        return transactionRepository.getAllByUserPerforming_Id(userId);
    }

    public List<Transaction> getTransactionHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Transaction> transactions = transactionRepository.getAllByUserPerforming_Id(userRepository.getUserIdByUsername(authentication.getName()));
        if (!transactions.isEmpty()){
            return transactions;
        }
        else { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No transactions found"); }
    }

    //method to check if the amount will not be over day limit
    public boolean underDayLimit(BigDecimal amount, User performerUser) {
        //get total amount of the previous transactions
        BigDecimal totalAmount = transactionRepository.getTotalAmountOfTransactionsByUserId(performerUser.getId());

        //check if the day spent stays below the day limit
        if (totalAmount.add(amount).compareTo(performerUser.getDayLimit()) >= 0)
            return true;
        return false;
    }

    //method to update the balance
    public void updateBalance(Account performerAccount, Account receiverAccount, BigDecimal amount) {
        //get balance from performer account
        BigDecimal performerBalance = accountRepository.getBalanceByIban(performerAccount.getIban(), performerAccount.getType());

        //get balance from receiver account
        BigDecimal receiverBalance = accountRepository.getBalanceByIban(receiverAccount.getIban(), receiverAccount.getType());

        //take amount from performer and add to receiver
        BigDecimal newPerformerBalance = performerBalance.subtract(amount);
        BigDecimal newReceiverBalance = receiverBalance.add(amount);

        //update balance of performer
        transactionRepository.UpdateBalance(newPerformerBalance, performerAccount.getIban(), performerAccount.getType());

        //update balance of receiver
        transactionRepository.UpdateBalance(newReceiverBalance, receiverAccount.getIban(), receiverAccount.getType());
    }

    //method to determine validity of the transaction
    private boolean validTransaction(Account performer, Account receiver) {
        int valid = 0;

        //check if the iban is not the same
        if (!performer.getIban().equals(receiver.getIban())) {
            valid++;
        }

        //check if the performer is a current and the receiver a current
        if (!performer.getType() && !receiver.getType()) {
            valid++;
        }

        //if both true then it is a valid withdrawal
        return valid == 2;
    }

    //method to determine validity of the deposit
    private boolean validDeposit(Account performer, Account receiver) {
        int valid = 0;

        //check if the iban is the same
        if (!performer.getIban().equals(receiver.getIban())) {
            valid++;
        }

        //check if the performer is a current and the receiver a savings
        if (!performer.getType() && receiver.getType()) {
            valid++;
        }

        //if both true then it is a valid withdrawal
        return valid == 2;
    }

    //method to determine validity of the withdrawal
    private boolean validWithdrawal(Account performer, Account receiver) {
        int valid = 0;

        //check if the iban is not the same
        if (!performer.getIban().equals(receiver.getIban())) {
            valid++;
        }

        //check if the performer is a savings and the receiver a current
        if (performer.getType() && !receiver.getType()) {
            valid++;
        }

        //if both true then it is a valid withdrawal
        return valid == 2;
    }

    //method to check if the iban exists and the amount is not below zero
    public boolean ibanAndAmountCheck(BigDecimal amount, String receiverIban) {
        //check first if the iban exists
        if (accountRepository.getAccountByIban(receiverIban) == null)
            return true;

        //check if amount is less than zero
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return true;
        return false;
    }

    //method to perform transaction
    public TransactionResponseDTO performTransaction(TransactionDTO body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //get user by username
        User performerUser = userRepository.findByUsername(authentication.getName());

        //get the account of the logged in user
        Account performerAccount = accountRepository.getAccountByUserIdAndType(performerUser.getId(), false);

        //get the account that will be used performing the transaction
        Account usedAccount = accountRepository.getAccountByIbanAndType(body.getPerformerIban(), false);

        //get the receiver account
        Account receiverAccount = accountRepository.getAccountByIbanAndType(body.getTargetIban(), false);

        //check if the used account is the same
        if (!usedAccount.getIban().equals(performerAccount.getIban())) {
            //check if the user has employee rights
            if (!performerUser.getRoles().contains(Role.ROLE_ADMIN)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You need employee rights to perform a transaction with someone else's account.");
            }
            if (usedAccount.getIban().equals("NL01INHO0000000001")) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You cannot use the bank's account.");
            }
        }

        //check if the used account exists
        if (usedAccount.getIban() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The filled in iban does not exist");
        }

        //check if the amount is not below zero and if the iban exists
        if (ibanAndAmountCheck(body.getAmount(), body.getTargetIban())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount was below zero or the Iban was not found.");
        }

        //check if the the transaction stays under the day limit
        if (underDayLimit(body.getAmount(), performerUser)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You went over your day limit, so this transaction cannot be made");
        }

        //check if it is a valid transaction
        if (!validTransaction(usedAccount, receiverAccount)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "To make a transaction, the receiver account must have another iban");
        }

        //check if the performers balance is not below the absolute limit
        if (usedAccount.getBalance().subtract(body.getAmount()).compareTo(usedAccount.getAbsoluteLimit()) < 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The requested amount to be transferred is below the absolute limit, and thus not possible");
        }

        //check transaction limit of the user
        if (body.getAmount().compareTo(performerUser.getTransactionLimit()) > 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot make transaction, because the amount to be transferred is higher than the transaction limit");
        }

        //Make the transaction
        Transaction transaction = makeTransaction(body.getAmount(), receiverAccount, usedAccount, TransferType.TYPE_TRANSACTION);

        //save the transaction
        transactionRepository.save(transaction);

        //make a transaction response
        TransactionResponseDTO responseDTO = createResponse(transaction);

        //execute transaction
        updateBalance(usedAccount, receiverAccount, body.getAmount());
        return responseDTO;
    }

    //method to perform a special transaction
    public TransactionResponseDTO performSpecialTransaction(DepositWithdrawalDTO body, TransferType transferType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //get user by username
        User performerUser = userRepository.findByUsername(authentication.getName());

        //get current en savings account from the user
        Account currentAccount = accountRepository.getAccountByUserIdAndTypeIsFalse(performerUser.getId());
        Account savingsAccount = accountRepository.getAccountByUserIdAndTypeIsTrue(performerUser.getId());

        //check if the amount is not below zero and if the iban exists
        if (ibanAndAmountCheck(body.getAmount(), savingsAccount.getIban())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount was below zero or the Iban was not found.");
        }

        //check the transfer type
        if (transferType == TransferType.TYPE_DEPOSIT) {
            //check for a valid deposit
            if (!validDeposit(currentAccount, savingsAccount)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You cannot transfer money directly from your current account to another account's savings account");
            }

            //check if the performers balance is not below the absolute limit
            if (currentAccount.getBalance().subtract(body.getAmount()).compareTo(currentAccount.getAbsoluteLimit()) < 0) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The requested amount to be transferred is below the absolute limit, and thus not possible");
            }
        }
        else if (transferType == TransferType.TYPE_WITHDRAW) {
            //check for valid withdrawal
            if (!validWithdrawal(savingsAccount, currentAccount)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You cannot transfer money directly from your savings account to another account's current account");
            }

            //determine if the performers balance is not below the absolute limit
            if (savingsAccount.getBalance().subtract(body.getAmount()).compareTo(savingsAccount.getAbsoluteLimit()) < 0) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The requested amount to be transferred is below the absolute limit, and thus not possible");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "The requested transfer type doesn't appear to exist");
        }

        //make the special transaction
        Transaction specialTransaction = makeTransaction(body.getAmount(), savingsAccount, currentAccount, transferType);

        //make a transaction response
        TransactionResponseDTO responseDTO = createResponse(specialTransaction);

        //save the special transaction
        transactionRepository.save(specialTransaction);

        //execute the special transaction
        if (transferType == TransferType.TYPE_WITHDRAW)
            updateBalance(savingsAccount, currentAccount, body.getAmount());
        else
            updateBalance(currentAccount, savingsAccount, body.getAmount());
        return responseDTO;
    }

    //method to create a transaction response
    private TransactionResponseDTO createResponse(Transaction transaction) {
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setAmount(transaction.getAmount());
        responseDTO.setPerformDate(transaction.getDate());
        responseDTO.setPerformingIban(transaction.getFromAccount().getIban());
        responseDTO.setPerformingUser(transaction.getUserPerforming().getUsername());
        responseDTO.setTargetIban(transaction.getToAccount().getIban());
        return responseDTO;
    }

    //method to perform a transaction
    public Transaction makeTransaction(BigDecimal amount, Account receiverAccount, Account performerAccount, TransferType transferType) {
        //prepare the transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        java.sql.Date timeOfTransaction = convertNowToString();
        transaction.setDate(timeOfTransaction);
        transaction.setUserPerforming(performerAccount.getUser());
        transaction.setFromAccount(performerAccount);
        transaction.setToAccount(receiverAccount);
        transaction.setTransferType(transferType);
        return transaction;
    }

    //method to get current date
    public java.sql.Date convertNowToString() {
        return new java.sql.Date(Calendar.getInstance().getTime().getTime());
    }
}
