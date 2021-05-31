package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.Deposit;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //get account by iban
    public Account getUserAccountByIban(String iban) {
        return accountRepository.getAccountByIban(iban);
    }

    public void updateBalance(Account performerAccount, Account receiverAccount, BigDecimal amount) {
        try {
            //savings to current
//            if (performerAccount.getType() && !receiverAccount.getType() && performerAccount.getIban().equals(receiverAccount.getIban())) {
//
//            }
            //current to savings
//            if (!performerAccount.getType() && receiverAccount.getType() && performerAccount.getIban().equals(receiverAccount.getIban())) {
//
//            }

            //get balance from performer account
            BigDecimal performerBalance = getBalanceByIban(performerAccount.getIban());

            //get balance from receiver account
            BigDecimal receiverBalance = getBalanceByIban(receiverAccount.getIban());

            //take amount from performer and add to receiver
            BigDecimal newPerformerBalance = performerBalance.subtract(amount);
            BigDecimal newReceiverBalance = receiverBalance.add(amount);

            //update balance of performer
            accountRepository.UpdateBalance(performerAccount.getIban(), newPerformerBalance);

            //update day spent of performer
            BigDecimal daySpent = getDaySpent(performerAccount.getUser().getId());
            BigDecimal newDaySpent = daySpent.add(amount);
            updateDaySpent(performerAccount.getUser().getId(), newDaySpent);

            //update balance of receiver
            accountRepository.UpdateBalance(receiverAccount.getIban(), newReceiverBalance);
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

    public BigDecimal getBalanceByIban(String iban) {
        return accountRepository.getBalanceByIban(iban);
    }

    //TODO manier vinden om dayspent te resetten
    public Transaction PerformTransaction(Long performerUserId, BigDecimal amount, String receiverIban) throws Exception {
        //get the account performing that wants to perform the transaction
        Account performerAccount = getUserAccountById(performerUserId);

        //get the performer user (maybe user will be the parameter instead of userid in the future)
        User performerUser = userService.getUserById(performerUserId);

        //get the receiver account
        Account receiverAccount = getUserAccountByIban(receiverIban);

        //determine if the performers balance is not below the absolute limit
        if (performerAccount.getBalance().subtract(amount).compareTo(performerAccount.getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit");
        }

        //determine if the user spent below the day limit
        if (performerUser.getDaySpent().compareTo(performerUser.getDayLimit()) > 0) {
            throw new Exception("Cannot make transaction, because you already spent more than your day limit");
        }

        //determine if the amount is higher than the transaction limit
        if (amount.compareTo(performerUser.getTransactionLimit()) > 0) {
            throw new Exception("Cannot make transaction, because the amount to be transferred is higher than the transaction limit");
        }
        //Make the transaction
        Transaction transaction = performerAccount.MakeTransaction(amount, receiverAccount, performerAccount);

        //save the transaction
        transactionService.SaveTransaction(transaction);

        //execute transaction
        updateBalance(performerAccount, receiverAccount, amount);
        return transaction;
    }

    public Deposit PerformDeposit(Long performerUserId, BigDecimal amount, String receiverIban) throws Exception {
        //get the account performing that wants to perform the transaction
        Account performerAccount = getUserAccountById(performerUserId);

        //get the performer user (maybe user will be the parameter instead of userid in the future)
        User performerUser = userService.getUserById(performerUserId);

        //get the receiver account
        Account receiverAccount = getUserAccountByIban(receiverIban);

        //determine if the performers balance is not below the absolute limit
        if (performerAccount.getBalance().subtract(amount).compareTo(performerAccount.getAbsoluteLimit()) < 0) {
            throw new Exception("The requested amount to be transferred is below the absolute limit");
        }

        //determine if the user spent below the day limit
        if (performerUser.getDaySpent().compareTo(performerUser.getDayLimit()) > 0) {
            throw new Exception("Cannot make transaction, because you already spent more than your day limit");
        }

        //determine if the amount is higher than the transaction limit
        if (amount.compareTo(performerUser.getTransactionLimit()) > 0) {
            throw new Exception("Cannot make transaction, because the amount to be transferred is higher than the transaction limit");
        }

        //make the deposit
        Deposit deposit = performerAccount.MakeDeposit(amount, receiverAccount, performerAccount);
        return deposit;
    }
}
