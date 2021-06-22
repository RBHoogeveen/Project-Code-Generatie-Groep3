package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.DTO.CreateUpdateAccountDTO;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(CreateUpdateAccountDTO createUpdateAccount) {
        Account updatedAccount;
        if (userRepository.findByUsername(createUpdateAccount.getUsername()) != null) {
            if (!createUpdateAccount.getType()) {
                updatedAccount = accountRepository.getAccountByUserIdAndTypeIsFalse(userRepository.getUserIdByUsername(createUpdateAccount.getUsername()));
            } else if (createUpdateAccount.getType()) {
                updatedAccount = accountRepository.getAccountByUserIdAndTypeIsTrue(userRepository.getUserIdByUsername(createUpdateAccount.getUsername()));
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't find users correct account.");
            }
            if (createUpdateAccount.getAbsoluteLimit().compareTo(BigDecimal.ZERO) >= 0) {
                updatedAccount.setAbsoluteLimit(createUpdateAccount.getAbsoluteLimit());
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Absolute limit can't be lower than 0");
            }
            if (userRepository.findByUsername(createUpdateAccount.getUsername()) != null) {
                updatedAccount.setUser(userRepository.findByUsername(createUpdateAccount.getUsername()));
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't find user with given username.");
            }
            if (createUpdateAccount.getBalance().compareTo(updatedAccount.getAbsoluteLimit()) >= 0) {
                updatedAccount.setBalance(createUpdateAccount.getBalance());
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "New balance is below absolute limit");
            }
            updatedAccount.setIsActive(createUpdateAccount.getIsActive());
            accountRepository.save(updatedAccount);
            return updatedAccount;
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User does not exist.");
        }
    }

    public Account add(CreateUpdateAccountDTO createUpdateAccount) {
        if ((accountRepository.getAccountByUserIdAndTypeIsFalse(userRepository.getUserIdByUsername(createUpdateAccount.getUsername())) == null) || (accountRepository.getAccountByUserIdAndTypeIsTrue(userRepository.getUserIdByUsername(createUpdateAccount.getUsername())) == null)){
            Account newAccount = new Account();
            newAccount.setIban(generateIban());
            newAccount.setType(createUpdateAccount.getType());
            if (createUpdateAccount.getAbsoluteLimit().compareTo(BigDecimal.ZERO) >= 0) {
                newAccount.setAbsoluteLimit(createUpdateAccount.getAbsoluteLimit());
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Absolute limit can't be lower than 0");
            }
            if (userRepository.findByUsername(createUpdateAccount.getUsername()) != null) {
                newAccount.setUser(userRepository.findByUsername(createUpdateAccount.getUsername()));
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Can't find user with given username.");
            }
            if (createUpdateAccount.getBalance().compareTo(createUpdateAccount.getAbsoluteLimit()) >= 0) {
                newAccount.setBalance(createUpdateAccount.getBalance());
            } else {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "New balance is below absolute limit");
            }
            newAccount.setIsActive(createUpdateAccount.getIsActive());
            accountRepository.save(newAccount);
            return newAccount;
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already has both account types.");
        }
    }

    public String generateIban() {
        Iban iban = new Iban.Builder().countryCode(CountryCode.NL).bankCode("INHO").buildRandom();
        while (accountRepository.getAccountByIban(iban.toString()) != null) {
            iban = new Iban.Builder().countryCode(CountryCode.NL).bankCode("INHO").buildRandom();
        }
        return iban.toString();
    }

    public void createCurrentAccount(String username) {
        Account newAccount = new Account();
        newAccount.setIban(generateIban());
        newAccount.setType(false);
        newAccount.setUser(userRepository.findByUsername(username));
        newAccount.setAbsoluteLimit(BigDecimal.ZERO);
        newAccount.setBalance(BigDecimal.valueOf(1000));
        newAccount.setIsActive(true);
        accountRepository.save(newAccount);
    }

    public void createSavingsAccount(String username) {
        Account newAccount = new Account();
        newAccount.setIban(generateIban());
        newAccount.setType(true);
        newAccount.setUser(userRepository.findByUsername(username));
        newAccount.setAbsoluteLimit(BigDecimal.ZERO);
        newAccount.setBalance(BigDecimal.valueOf(2000));
        newAccount.setIsActive(true);
        accountRepository.save(newAccount);
    }


    public List<?> getTransferHistory() {
        List<Object> transfers = new ArrayList<>();
        if (transactionRepository.getAllByUserPerforming_Id(userRepository.getUserIdByUsername(authentication.getName())) != null) {
            transfers.addAll(transactionService.getTransactionHistory());
        }
        if (!transfers.isEmpty()){
            return transfers;
        }
        else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No transfers found.");}
    }

    public List<Account> getUserAccounts(String username) {
        List<Account> accounts = new ArrayList<>();
        if (accountRepository.getAccountByUserIdAndTypeIsFalse(userRepository.getUserIdByUsername(username)) != null){
            accounts.add(accountRepository.getAccountByUserIdAndTypeIsFalse(userRepository.getUserIdByUsername(username)));
        }
        if (accountRepository.getAccountByUserIdAndTypeIsTrue(userRepository.getUserIdByUsername(username)) != null){
            accounts.add(accountRepository.getAccountByUserIdAndTypeIsTrue(userRepository.getUserIdByUsername(username)));
        }
        if(!accounts.isEmpty()){
            return accounts;
        }
        else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No accounts found for user: " + username);}
    }

    public Account getAccountByIban(String iban){
        Account account = accountRepository.getAccountByIban(iban);
        if (account != null){
            return account;
        }
        else {throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No accounts found for iban: " + iban);}
    }
}
