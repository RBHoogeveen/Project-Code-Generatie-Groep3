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
import static org.assertj.core.api.Assertions.assertThat;

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
    assertThat(accountRepo.getBalanceByIban("NL01INHO0000000001", false)).isEqualTo(new BigDecimal("999999999.00"));
  }

  @Test
  public void getAccountByIbanAndTypeShouldReturnAccount(){
    assertThat(accountRepo.getAccountByIbanAndType("NL01INHO0000000001", false).getClass()).isEqualTo(Account.class);
  }

  @Test
  public void getAccountByUserIdAndTypeShouldReturnAccount(){
    assertThat(accountRepo.getAccountByUserIdAndType(3, false).getClass()).isEqualTo(Account.class);
  }

  @Test
  public void getAccountByUserIdAndTypeIsFalseShouldReturnAccount(){
    assertThat(accountRepo.getAccountByUserIdAndTypeIsFalse(3).getClass()).isEqualTo(Account.class);
  }

  @Test
  public void getAccountByUserIdAndTypeIsTrueShouldReturnAccount(){
    assertThat(accountRepo.getAccountByUserIdAndTypeIsTrue(3).getClass()).isEqualTo(Account.class);
  }

}
