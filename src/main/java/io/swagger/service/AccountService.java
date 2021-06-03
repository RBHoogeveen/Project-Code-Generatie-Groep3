package io.swagger.service;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    DepositService depositService;

    @Autowired
    WithdrawalService withdrawalService;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    //get account by userid
    public Account getUserAccountById(Long userId) {
        return accountRepository.getAccountByUserId(userId);
    }

    //get saving account by userid
    public Account getSavingAccountByUserId(Integer userId) {
        return accountRepository.getCorrectAccountByUserId(userId, true);
    }

    //get current account by userid
    public Account getCurrentAccountByUserId(Integer userId) {
        return accountRepository.getCorrectAccountByUserId(userId, false);
    }

    //get account by iban
    public Account getUserAccountByIban(String iban) {
        return accountRepository.getAccountByIban(iban);
    }

    //method to get savings account
    public Account getSavingsAccountByIban(String iban) {
        return accountRepository.getCorrectAccountByIban(iban, true);
    }

    //method to get current account
    public Account getCurrentAccountByIban(String iban) {
        return accountRepository.getCorrectAccountByIban(iban, false);
    }

    //method to check if the amount will not be over day limit
    public boolean UnderDayLimit(BigDecimal amount, TransferType transferType, User performerUser) {
        List<Transaction> transactions;
        List<Deposit> deposits;
        List<Withdrawal> withdrawals;

        //make an empty account to access the time method
        Account account = new Account();
        String now = account.convertNowToString();

        BigDecimal daySpent;
        BigDecimal dayLimit;
        switch (transferType) {
            case TYPE_TRANSACTION:
                transactions = transactionService.getTransactionsByUser(performerUser);
                if (transactions.size() == 0)
                    return false;

                //get the day spent and the day limit of the user
                daySpent = transactions.get(0).getUserPerforming().getDaySpent();
                dayLimit = transactions.get(0).getUserPerforming().getDaySpent();

                //calculate total day spent of the transactions on a specific day
                for (Transaction transaction: transactions) {
                    if (transaction.getDate().compareTo(now) == 0) {
                        daySpent = daySpent.add(transaction.getAmount());
                    }
                }
                break;

            case TYPE_DEPOSIT:
                deposits = depositService.getDepositsByUser(performerUser);
                if (deposits.size() == 0)
                    return false;

                //get the day spent and the day limit of the user
                daySpent = deposits.get(0).getUserPerforming().getDaySpent();
                dayLimit = deposits.get(0).getUserPerforming().getDaySpent();

                //calculate total day spent of the transactions on a specific day
                for (Deposit deposit: deposits) {
                    if (deposit.getDate().compareTo(now) == 0) {
                        daySpent = daySpent.add(deposit.getAmount());
                    }
                }
                break;

            case TYPE_WITHDRAW:
                withdrawals = withdrawalService.getWithdrawalsByUser(performerUser);
                if (withdrawals.size() == 0)
                    return false;

                //get the day spent and the day limit of the user
                daySpent = withdrawals.get(0).getUserPerforming().getDaySpent();
                dayLimit = withdrawals.get(0).getUserPerforming().getDaySpent();

                //calculate total day spent of the transactions on a specific day
                for (Withdrawal withdrawal: withdrawals) {
                    if (withdrawal.getDate().compareTo(now) == 0) {
                        daySpent = daySpent.add(withdrawal.getAmount());
                    }
                }
                break;

            default:
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Something went wrong while processing the transfer");
        }

        //check if the day spent stays below the day limit
        if (daySpent.add(amount).compareTo(dayLimit) >= 0)
            return true;
        return false;
    }

    //method to update the balance
    public void updateBalance(Account performerAccount, Account receiverAccount, BigDecimal amount) {
        try {
            //get balance from performer account
            BigDecimal performerBalance = getBalanceByIban(performerAccount.getIban(), performerAccount.getType());

            //get balance from receiver account
            BigDecimal receiverBalance = getBalanceByIban(receiverAccount.getIban(), receiverAccount.getType());

            //take amount from performer and add to receiver
            BigDecimal newPerformerBalance = performerBalance.subtract(amount);
            BigDecimal newReceiverBalance = receiverBalance.add(amount);

            //update balance of performer
            accountRepository.UpdateBalance(newPerformerBalance, performerAccount.getIban(), performerAccount.getType());

            //update day spent of performer
            BigDecimal daySpent = getDaySpent(performerAccount.getUser().getId());
            BigDecimal newDaySpent = daySpent.add(amount);
            updateDaySpent(performerAccount.getUser().getId(), newDaySpent);

            //update balance of receiver
            accountRepository.UpdateBalance(newReceiverBalance, receiverAccount.getIban(), receiverAccount.getType());
        } catch (Exception e) {
            //TODO exception handling
        }
    }

    public BigDecimal getDaySpent(Integer userId) {
        return userService.getDaySpent(userId);
    }

    public void updateDaySpent(Integer userId, BigDecimal newDaySpent) {
        userService.updateDaySpent(userId, newDaySpent);
    }

    public BigDecimal getBalanceByIban(String iban, boolean accountType) {
        return accountRepository.getBalanceByIban(iban, accountType);
    }

    //method to check day limit en transaction limit of the user
    void CheckDayLimitAndTransactionLimit(BigDecimal amount, User performerUser) throws Exception {
        //determine if the user spent below the day limit
        if (performerUser.getDaySpent().compareTo(performerUser.getDayLimit()) > 0) {
            throw new Exception("Cannot make transaction, because you already spent more than your day limit");
        }

        //determine if the amount is higher than the transaction limit
        if (amount.compareTo(performerUser.getTransactionLimit()) > 0) {
            throw new Exception("Cannot make transaction, because the amount to be transferred is higher than the transaction limit");
        }
    }

    //method to get the performer account and receiver
    public Account[] GetPerformerAndReceiver(Integer performerUserId, String receiverIban, TransferType transferType) throws Exception {
        Account[] performerAndReceiver = new Account[2];
        switch (transferType) {
            //current to current
            case TYPE_TRANSACTION:
                performerAndReceiver[0] = getCurrentAccountByUserId(performerUserId);
                performerAndReceiver[1] =  getCurrentAccountByIban(receiverIban);

                //check if the transaction is valid
                if (!ValidTransaction(performerAndReceiver))
                    throw new Exception("To make a transaction, the receiver account must have another iban");
                break;
            case TYPE_DEPOSIT:
                //current to savings
                performerAndReceiver[0] = getCurrentAccountByUserId(performerUserId);
                performerAndReceiver[1] =  getSavingsAccountByIban(receiverIban);

                //check if the deposit is valid
                if (!ValidDeposit(performerAndReceiver))
                    throw new Exception("You cannot transfer money directly from your current account to another account's savings account");

                break;
            case TYPE_WITHDRAW:
                //savings to current
                performerAndReceiver[0] = getSavingAccountByUserId(performerUserId);
                performerAndReceiver[1] = getCurrentAccountByIban(receiverIban);

                //check if the withdrawal is valid
                if (!ValidWithdrawal(performerAndReceiver))
                    throw new Exception("You cannot transfer money directly from your savings account to another account's current account");
                break;
        }
        return performerAndReceiver;
    }

    //method to determine validity of the transaction
    private boolean ValidTransaction(Account[] performerAndReceiver) {
        int valid = 0;

        //check if the iban is not the same
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            valid++;
        }

        //check if the performer is a current and the receiver a current
        if (!performerAndReceiver[0].getType() && !performerAndReceiver[1].getType()) {
            valid++;
        }

        //if both true then it is a valid withdrawal
        return valid == 2;
    }

    //method to determine validity of the deposit
    private boolean ValidDeposit(Account[] performerAndReceiver) {
        int valid = 0;

        //check if the iban is the same
        if (performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            valid++;
        }

        //check if the performer is a current and the receiver a savings
        if (!performerAndReceiver[0].getType() && performerAndReceiver[1].getType()) {
            valid++;
        }

        //if both true then it is a valid withdrawal
        return valid == 2;
    }

    //method to determine validity of the withdrawal
    private boolean ValidWithdrawal(Account[] performerAndReceiver) {
        int valid = 0;

        //check if the iban is the same
        if (performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            valid++;
        }

        //check if the performer is a savings and the receiver a current
        if (performerAndReceiver[0].getType() && !performerAndReceiver[1].getType()) {
            valid++;
        }

        //if both true then it is a valid withdrawal
        return valid == 2;
    }

    //method to check if the account is a current or savings
    public boolean CheckAccount(Account account) {
        //false = current account, true = savings account
        return account.getType();
    }

    public boolean IbanAndAmountCheck(BigDecimal amount, String receiverIban) {
        //check first if the iban exists
        if (accountRepository.getIban(receiverIban).equals(""))
            return true;

        //check if amount is less than zero
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            return true;
        return false;
    }

    //method to perform transaction
    public Transaction PerformTransaction(String username, BigDecimal amount, String receiverIban) throws Exception {
        if (IbanAndAmountCheck(amount, receiverIban)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount was below zero or the Iban was not found.");
        }

        //get user by username
        User performerUser = userService.getUserByUsername(username);

        if (UnderDayLimit(amount, TransferType.TYPE_TRANSACTION, performerUser)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You went over your day limit, so this transaction cannot be made");
        }

        //get the performer user (maybe user will be the parameter instead of userid in the future)

        Account[] performerAndReceiver = GetPerformerAndReceiver(performerUser.getId(), receiverIban, TransferType.TYPE_TRANSACTION);

        //determine if the performers balance is not below the absolute limit
        if (performerAndReceiver[0].getBalance().subtract(amount).compareTo(performerAndReceiver[0].getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit, and thus not possible");
        }

        //check day limit and transaction limit of the user if it is a transaction
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            CheckDayLimitAndTransactionLimit(amount, performerUser);
        }

        //Make the transaction
        Transaction transaction = performerAndReceiver[0].MakeTransaction(amount, performerAndReceiver[1], performerAndReceiver[0]);

        //save the transaction
        transactionService.SaveTransaction(transaction);

        //execute transaction
        updateBalance(performerAndReceiver[0], performerAndReceiver[1], amount);
        return transaction;
    }

    //method to perform deposit
    public Deposit PerformDeposit(String username, BigDecimal amount, String receiverIban) throws Exception {
        if (IbanAndAmountCheck(amount, receiverIban)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount was below zero or the Iban was not found.");
        }

        //get user by username
        User performerUser = userService.getUserByUsername(username);

        if (UnderDayLimit(amount, TransferType.TYPE_DEPOSIT, performerUser)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You went over your day limit, so this deposit cannot be made");
        }

        Account[] performerAndReceiver = GetPerformerAndReceiver(performerUser.getId(), receiverIban, TransferType.TYPE_DEPOSIT);

        //determine if the performers balance is not below the absolute limit
        if (performerAndReceiver[0].getBalance().subtract(amount).compareTo(performerAndReceiver[0].getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit, and thus not possible");
        }

        //check day limit and transaction limit of the user if it is a transaction
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            CheckDayLimitAndTransactionLimit(amount, performerUser);
        }

        //make the deposit
        Deposit deposit = performerAndReceiver[0].MakeDeposit(amount, performerAndReceiver[1], performerAndReceiver[0]);

        //save the deposit
        depositService.SaveDeposit(deposit);

        //execute deposit
        updateBalance(performerAndReceiver[0], performerAndReceiver[1], amount);
        return deposit;
    }

    //method to perform deposit
    public Withdrawal PerformWithdrawal(String username, BigDecimal amount, String receiverIban) throws Exception {
        if (IbanAndAmountCheck(amount, receiverIban)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount was below zero or the Iban was not found.");
        }

        //get user by username
        User performerUser = userService.getUserByUsername(username);

        if (UnderDayLimit(amount, TransferType.TYPE_WITHDRAW, performerUser)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You went over your day limit, so this withdrawal cannot be made");
        }

        Account[] performerAndReceiver = GetPerformerAndReceiver(performerUser.getId(), receiverIban, TransferType.TYPE_WITHDRAW);

        //determine if the performers balance is not below the absolute limit
        if (performerAndReceiver[0].getBalance().subtract(amount).compareTo(performerAndReceiver[0].getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit, and thus not possible");
        }

        //check day limit and transaction limit of the user if it is a transaction
        if (!performerAndReceiver[0].getIban().equals(performerAndReceiver[1].getIban())) {
            CheckDayLimitAndTransactionLimit(amount, performerUser);
        }

        //make the deposit
        Withdrawal withdrawal = performerAndReceiver[0].MakeWithdrawal(amount, performerAndReceiver[1], performerAndReceiver[0]);

        //save the deposit
        withdrawalService.SaveWithdrawal(withdrawal);

        //execute deposit
        updateBalance(performerAndReceiver[0], performerAndReceiver[1], amount);
        return withdrawal;
    }
}
