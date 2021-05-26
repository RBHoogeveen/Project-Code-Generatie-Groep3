package io.swagger.api.tempservice;

import io.swagger.api.temprepository.AccountRepository;
import io.swagger.model.Account;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    //get account by userid
    public Account getUserAccountById(Long userId) {
        return accountRepository.getAccountByUserId(userId);
    }

   //get account by iban
   public Account getUserAccountByIban(Iban iban) {
        return accountRepository.getAccountByIban(iban);
   }

   //perform transaction
    public void PerformTransAction() {

    }
}
