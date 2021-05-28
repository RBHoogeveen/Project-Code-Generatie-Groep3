package io.swagger.service;

import io.swagger.model.Account;
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

    public void updateBalance() {

    }

    //TODO update balance bij performer en receiver
    //TODO update daySpent bij performer
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
        transactionService.SaveTransaction(transaction);
        return transaction;
    }
}
