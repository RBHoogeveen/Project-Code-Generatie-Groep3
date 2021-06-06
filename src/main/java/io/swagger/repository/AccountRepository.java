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
    @Query(value = "SELECT * FROM Account WHERE user_ID = ?1", nativeQuery = true)
    List<Account> getAccountByUserId(Long userId);

    @Query(value = "SELECT * FROM Account WHERE iban = ?1", nativeQuery = true)
    Account getAccountByIban(String iban);

    @Query(value = "SELECT balance FROM Account WHERE iban = ?1 AND type = ?2")
    BigDecimal getBalanceByIban(String iban, boolean accountType);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account SET balance = ?1 WHERE iban = ?2 AND type = ?3", nativeQuery = true)
    void UpdateBalance(BigDecimal newBalance, String iban, boolean accountType);

    @Query(value = "SELECT iban FROM Account WHERE iban = ?1")
    String getIban(String iban);

    @Query(value = "SELECT * FROM Account WHERE iban = ?1 AND type = ?2", nativeQuery = true)
    Account getCorrectAccountByIban(String iban, boolean savingsAccount);

    @Query(value = "SELECT * FROM Account WHERE user_ID = ?1 AND type = ?2", nativeQuery = true)
    Account getCorrectAccountByUserId(Integer userId, boolean savingsAccount);

    @Query(value = "SELECT * FROM Account WHERE user_id = ?1 AND type = false", nativeQuery = true)
    Account getCurrentAccountByUserId(Integer userid);

    @Query(value = "SELECT * FROM Account WHERE user_id = ?1 AND type = true", nativeQuery = true)
    Account getSavingsAccountByUserId(Integer userid);

    @Query(value = "SELECT * FROM ACCOUNT where user_id = (SELECT ID FROM user WHERE username = '?1')", nativeQuery = true)
    List<Account> getAccountByUserId(String username);
}
