package io.swagger.repository;

import io.swagger.model.Account;
import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getAccountByIban(String iban);

    @Query(value = "SELECT balance FROM Account WHERE iban = ?1 AND type = ?2")
    BigDecimal getBalanceByIban(String iban, boolean accountType);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account SET balance = ?1 WHERE iban = ?2 AND type = ?3")
    void UpdateBalance(BigDecimal newBalance, String iban, boolean accountType);

    @Query(value = "SELECT iban FROM Account WHERE iban = ?1")
    String getIban(String iban);

    Account getAccountByIbanAndType(String iban, boolean accountType);

    Account getAccountByUserIdAndType(Integer userId, boolean accountType);

    Account getAccountByUserIdAndTypeIsFalse(Integer userid);

    Account getAccountByUserIdAndTypeIsTrue(Integer userId);
}
