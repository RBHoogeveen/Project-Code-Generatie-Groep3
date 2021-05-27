package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

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
    //perform transaction
    public void PerformTransAction() {

    }
}
