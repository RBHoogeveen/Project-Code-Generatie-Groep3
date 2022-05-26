package io.swagger.model;

import io.swagger.model.DTO.CreateUpdateAccountDTO;
import io.swagger.repository.UserRepository;
import io.swagger.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountTest {
  @Autowired
  private AccountService accountService;

  @Autowired
  private UserRepository userRepository;

  private Account account;

  @Before
  public void SetUp() {
    account = new Account();
  }

  @Test
  public void createAccountShouldNotBeNull() {
    assertNotNull(account);
  }

  @Test
  public void addAccountShouldReturnObject() {
    CreateUpdateAccountDTO testCreateUpdateAccount = new CreateUpdateAccountDTO();
    testCreateUpdateAccount.setUsername("Test");
    testCreateUpdateAccount.setIsActive(true);
    testCreateUpdateAccount.setType(false);
    testCreateUpdateAccount.setBalance(BigDecimal.valueOf(100));
    testCreateUpdateAccount.setAbsoluteLimit(BigDecimal.ZERO);

    Account response = accountService.add(testCreateUpdateAccount);

    // Assert that the account has been created
    assertEquals(userRepository.findByUsername(testCreateUpdateAccount.getUsername()), response.getUser());
    assertTrue(response.getIsActive());
    assertFalse(response.getType());
  }

  @Test
  public void updateAccountShouldReturnAccount() {
    CreateUpdateAccountDTO testCreateUpdateAccount = new CreateUpdateAccountDTO();
    testCreateUpdateAccount.setUsername("User");
    testCreateUpdateAccount.setIsActive(false);
    testCreateUpdateAccount.setType(false);
    testCreateUpdateAccount.setBalance(BigDecimal.valueOf(3000));
    testCreateUpdateAccount.setAbsoluteLimit(BigDecimal.ZERO);

    Account response = accountService.updateAccount(testCreateUpdateAccount);

    assertEquals(BigDecimal.valueOf(3000), response.getBalance());
    assertFalse(response.getIsActive());
  }

  @Test
  public void addAccountAbsoluteLimitBelowZeroShouldReturnException(){
    CreateUpdateAccountDTO testCreateUpdateAccount = new CreateUpdateAccountDTO();
    testCreateUpdateAccount.setUsername("Test");
    testCreateUpdateAccount.setIsActive(true);
    testCreateUpdateAccount.setType(false);
    testCreateUpdateAccount.setBalance(BigDecimal.valueOf(100));
    testCreateUpdateAccount.setAbsoluteLimit(BigDecimal.valueOf(-10));

    Exception exception = assertThrows(ResponseStatusException.class,
        () -> accountService.add(testCreateUpdateAccount));
    assertEquals("422 UNPROCESSABLE_ENTITY \"Absolute limit can't be lower than 0\"", exception.getMessage());
  }

  @Test
  public void updateAccountAbsoluteLimitBelowZeroShouldReturnException(){
    CreateUpdateAccountDTO testCreateUpdateAccount = new CreateUpdateAccountDTO();
    testCreateUpdateAccount.setUsername("Test");
    testCreateUpdateAccount.setIsActive(true);
    testCreateUpdateAccount.setType(false);
    testCreateUpdateAccount.setBalance(BigDecimal.valueOf(100));
    testCreateUpdateAccount.setAbsoluteLimit(BigDecimal.valueOf(-10));

    Exception exception = assertThrows(ResponseStatusException.class,
        () -> accountService.updateAccount(testCreateUpdateAccount));
    assertEquals("422 UNPROCESSABLE_ENTITY \"Absolute limit can't be lower than 0\"", exception.getMessage());
  }

  @Test
  public void addAccountWrongUsernameShouldReturnException(){
    CreateUpdateAccountDTO testCreateUpdateAccount = new CreateUpdateAccountDTO();
    testCreateUpdateAccount.setUsername("ThisUserDoesNotExist");
    testCreateUpdateAccount.setIsActive(true);
    testCreateUpdateAccount.setType(false);
    testCreateUpdateAccount.setBalance(BigDecimal.valueOf(100));
    testCreateUpdateAccount.setAbsoluteLimit(BigDecimal.ZERO);

    Exception exception = assertThrows(ResponseStatusException.class,
        () -> accountService.add(testCreateUpdateAccount));
    assertEquals("422 UNPROCESSABLE_ENTITY \"Can't find user with given username.\"", exception.getMessage());
  }

  @Test
  public void updateAccountWrongUsernameShouldReturnException(){
    CreateUpdateAccountDTO testCreateUpdateAccount = new CreateUpdateAccountDTO();
    testCreateUpdateAccount.setUsername("ThisUserDoesNotExist");
    testCreateUpdateAccount.setIsActive(true);
    testCreateUpdateAccount.setType(false);
    testCreateUpdateAccount.setBalance(BigDecimal.valueOf(100));
    testCreateUpdateAccount.setAbsoluteLimit(BigDecimal.ZERO);

    Exception exception = assertThrows(ResponseStatusException.class,
        () -> accountService.updateAccount(testCreateUpdateAccount));
    assertEquals("422 UNPROCESSABLE_ENTITY \"User does not exist.\"", exception.getMessage());
  }

  @Test
  public void addAccountToUserWithBothAccountsShouldReturnException(){
    CreateUpdateAccountDTO testCreateUpdateAccount = new CreateUpdateAccountDTO();
    testCreateUpdateAccount.setUsername("User");
    testCreateUpdateAccount.setIsActive(true);
    testCreateUpdateAccount.setType(false);
    testCreateUpdateAccount.setBalance(BigDecimal.valueOf(-10));
    testCreateUpdateAccount.setAbsoluteLimit(BigDecimal.ZERO);

    Exception exception = assertThrows(ResponseStatusException.class,
        () -> accountService.add(testCreateUpdateAccount));
    assertEquals("422 UNPROCESSABLE_ENTITY \"User already has both account types.\"", exception.getMessage());
  }

  @Test
  public void updateAccountBalanceBelowAbsoluteLimitShouldReturnException(){
    CreateUpdateAccountDTO testCreateUpdateAccount = new CreateUpdateAccountDTO();
    testCreateUpdateAccount.setUsername("User");
    testCreateUpdateAccount.setIsActive(true);
    testCreateUpdateAccount.setType(false);
    testCreateUpdateAccount.setBalance(BigDecimal.valueOf(-10));
    testCreateUpdateAccount.setAbsoluteLimit(BigDecimal.ZERO);

    Exception exception = assertThrows(ResponseStatusException.class,
        () -> accountService.updateAccount(testCreateUpdateAccount));
    assertEquals("422 UNPROCESSABLE_ENTITY \"New balance is below absolute limit\"", exception.getMessage());
  }

  @Test
  public void getAccountByIbanWithInvalidIbanShouldReturnException() {
    Exception exception = assertThrows(ResponseStatusException.class,
        () -> accountService.getAccountByIban("NotAnIban"));
    assertEquals("422 UNPROCESSABLE_ENTITY \"No accounts found for iban: NotAnIban\"", exception.getMessage());
  }

  @Test
  public void getUserAccountsWithNonExistentUserShouldReturnException() {
    Exception exception = assertThrows(ResponseStatusException.class,
        () -> accountService.getUserAccounts("NotAUser"));
    assertEquals("422 UNPROCESSABLE_ENTITY \"No accounts found for user: NotAUser\"", exception.getMessage());
  }

  @Test
  public void getUserAccountsUserShouldReturnListOfAccounts() {
    List<Account> userAccounts = accountService.getUserAccounts("User");

    assertEquals(2, userAccounts.size());
  }
}
