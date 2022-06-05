package io.swagger.repository;

import io.swagger.model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryTests {
  @Autowired
  private AccountRepository accountRepo;

  @Test
  public void getAccountByIbanShouldReturnAccount(){
    assertEquals(Account.class ,accountRepo.getAccountByIban("NL01INHO0000000001").getClass());
  }

  @Test
  public void getBalanceByIbanShouldReturnBigDecimal(){
    assertEquals(BigDecimal.valueOf(999999999.00), accountRepo.getBalanceByIban("NL01INHO0000000001", false));
  }

  @Test
  public void getAccountByIbanAndTypeShouldReturnAccount(){
    assertEquals(Account.class ,accountRepo.getAccountByIbanAndType("NL01INHO0000000001", false));
  }

  @Test
  public void getAccountByUserIdAndTypeShouldReturnAccount(){
    assertEquals(Account.class ,accountRepo.getAccountByUserIdAndType(3, false));
  }

  @Test
  public void getAccountByUserIdAndTypeIsFalseShouldReturnAccount(){
    assertEquals(Account.class ,accountRepo.getAccountByUserIdAndTypeIsFalse(3));
  }

  @Test
  public void getAccountByUserIdAndTypeIsTrueShouldReturnAccount(){
    assertEquals(Account.class ,accountRepo.getAccountByUserIdAndTypeIsTrue(3));
  }

}
