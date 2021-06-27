package io.swagger.repository;

import io.swagger.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  Account getAccountByIban(String iban);

  @Query(value = "SELECT balance FROM Account WHERE iban = ?1 AND type = ?2")
  BigDecimal getBalanceByIban(String iban, boolean accountType);

  Account getAccountByIbanAndType(String iban, boolean accountType);

  Account getAccountByUserIdAndType(Integer userId, boolean accountType);

  Account getAccountByUserIdAndTypeIsFalse(Integer userid);

  Account getAccountByUserIdAndTypeIsTrue(Integer userId);
}
