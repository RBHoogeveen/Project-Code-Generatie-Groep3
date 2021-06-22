package io.swagger;

import io.swagger.model.Account;
import io.swagger.model.DTO.CreateUpdateUserDTO;
import io.swagger.model.Role;
import io.swagger.model.TransferType;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private AccountService accountService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private TransactionRepository transactionRepository;
  @Autowired
  private TransactionService transactionService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    //make bank + bank accounts
    CreateUpdateUserDTO bank = new CreateUpdateUserDTO();
    bank.setCreateCurrentAccount(false);
    bank.setCreateSavingsAccount(false);
    bank.setIsActive(true);
    bank.setEmail("Bank@bank.nl");
    bank.setFirstname("Bank");
    bank.setLastname("Bank");
    bank.setDayLimit(BigDecimal.valueOf(10000000));
    bank.setPhonenumber("06-12121212");
    bank.setRoles(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
    bank.setTransactionLimit(BigDecimal.valueOf(10000000));
    bank.setUsername("Bank");
    bank.setPassword("Bank");
    userService.add(bank);

    Account bankAccount = new Account();
    bankAccount.setUser(userRepository.findByUsername(bank.getUsername()));
    bankAccount.setBalance(BigDecimal.valueOf(999999999));
    bankAccount.setIban("NL01INHO0000000001");
    bankAccount.setIsActive(true);
    bankAccount.setAbsoluteLimit(BigDecimal.ZERO);
    bankAccount.setType(false);
    accountRepository.save(bankAccount);

    //make admin + admin accounts
    CreateUpdateUserDTO admin = new CreateUpdateUserDTO();
    admin.setCreateCurrentAccount(false);
    admin.setCreateSavingsAccount(false);
    admin.setIsActive(true);
    admin.setEmail("Admin@admin.nl");
    admin.setFirstname("Admin");
    admin.setLastname("Admin");
    admin.setDayLimit(BigDecimal.valueOf(12345));
    admin.setPhonenumber("06-12345678");
    admin.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
    admin.setTransactionLimit(BigDecimal.valueOf(5000));
    admin.setUsername("Admin");
    admin.setPassword("Admin");
    userService.add(admin);

    Account adminCurrentAccount = new Account();
    adminCurrentAccount.setUser(userRepository.findByUsername(admin.getUsername()));
    adminCurrentAccount.setBalance(BigDecimal.valueOf(10000));
    adminCurrentAccount.setIban("NL02INHO0000000002");
    adminCurrentAccount.setIsActive(true);
    adminCurrentAccount.setAbsoluteLimit(BigDecimal.ZERO);
    adminCurrentAccount.setType(false);
    accountRepository.save(adminCurrentAccount);

    Account adminSavingsAccount = new Account();
    adminSavingsAccount.setUser(userRepository.findByUsername(admin.getUsername()));
    adminSavingsAccount.setBalance(BigDecimal.valueOf(20000));
    adminSavingsAccount.setIban("NL03INHO0000000003");
    adminSavingsAccount.setIsActive(true);
    adminSavingsAccount.setAbsoluteLimit(BigDecimal.ZERO);
    adminSavingsAccount.setType(true);
    accountRepository.save(adminSavingsAccount);

    //make user + user accounts
    CreateUpdateUserDTO user = new CreateUpdateUserDTO();
    user.setCreateCurrentAccount(false);
    user.setCreateSavingsAccount(false);
    user.setIsActive(true);
    user.setEmail("User@user.nl");
    user.setFirstname("User");
    user.setLastname("User");
    user.setDayLimit(BigDecimal.valueOf(54321));
    user.setPhonenumber("06-34343434");
    user.setRoles(Collections.singletonList(Role.ROLE_USER));
    user.setTransactionLimit(BigDecimal.valueOf(3000));
    user.setUsername("User");
    user.setPassword("User");
    userService.add(user);

    Account userCurrentAccount = new Account();
    userCurrentAccount.setUser(userRepository.findByUsername(user.getUsername()));
    userCurrentAccount.setBalance(BigDecimal.valueOf(10000));
    userCurrentAccount.setIban("NL04INHO0000000004");
    userCurrentAccount.setIsActive(true);
    userCurrentAccount.setAbsoluteLimit(BigDecimal.ZERO);
    userCurrentAccount.setType(false);
    accountRepository.save(userCurrentAccount);

    Account userSavingsAccount = new Account();
    userSavingsAccount.setUser(userRepository.findByUsername(user.getUsername()));
    userSavingsAccount.setBalance(BigDecimal.valueOf(20000));
    userSavingsAccount.setIban("NL05INHO0000000005");
    userSavingsAccount.setIsActive(true);
    userSavingsAccount.setAbsoluteLimit(BigDecimal.ZERO);
    userSavingsAccount.setType(true);
    accountRepository.save(userSavingsAccount);

    //make transactions
    //user current to admin current
    transactionService.makeTransaction(BigDecimal.valueOf(500), accountRepository.getAccountByIban("NL02INHO0000000002"), accountRepository.getAccountByIban("NL04INHO0000000004"), TransferType.TYPE_TRANSACTION);
    //admin current to user current
    transactionService.makeTransaction(BigDecimal.valueOf(500), accountRepository.getAccountByIban("NL04INHO0000000004"), accountRepository.getAccountByIban("NL02INHO0000000002"), TransferType.TYPE_TRANSACTION);

    //make deposits
    //admin current to admin savings
    transactionService.makeTransaction(BigDecimal.valueOf(1000), accountRepository.getAccountByIban("NL02INHO0000000002"), accountRepository.getAccountByIban("NL03INHO0000000003"), TransferType.TYPE_DEPOSIT);
    //user current to user savings
    transactionService.makeTransaction(BigDecimal.valueOf(1000), accountRepository.getAccountByIban("NL04INHO0000000004"), accountRepository.getAccountByIban("NL05INHO0000000005"), TransferType.TYPE_DEPOSIT);

    //make withdrawals
    //admin savings to admin current
    transactionService.makeTransaction(BigDecimal.valueOf(2000), accountRepository.getAccountByIban("NL03INHO0000000003"), accountRepository.getAccountByIban("NL02INHO0000000002"), TransferType.TYPE_WITHDRAW);
    //user savings to user current
    transactionService.makeTransaction(BigDecimal.valueOf(1000), accountRepository.getAccountByIban("NL05INHO0000000005"), accountRepository.getAccountByIban("NL04INHO0000000004"), TransferType.TYPE_WITHDRAW);
  }
}
