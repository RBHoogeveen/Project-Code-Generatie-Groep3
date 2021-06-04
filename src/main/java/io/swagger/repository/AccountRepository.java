package io.swagger.repository;

import io.swagger.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * FROM Account WHERE userID = ?1", nativeQuery = true)
    Account getAccountByUserId(Long userId);

    @Query(value = "SELECT * FROM Account WHERE iban = ?1", nativeQuery = true)
    Account getAccountByIban(String iban);

    @Query(value = "SELECT balance FROM Account WHERE iban = ?1", nativeQuery = true)
    BigDecimal getBalanceByIban(String iban);

    @Query(value = "UPDATE Account SET balance = ?1 WHERE iban = ?2", nativeQuery = true)
    void UpdateBalance(String iban, BigDecimal newBalance);

    @Query(value = "SELECT iban FROM Account WHERE iban = ?1")
    String getIban(String iban);

    @Query(value = "SELECT * FROM Account WHERE iban = ?1 AND type = ?2", nativeQuery = true)
    Account getCorrectAccountByIban(String iban, boolean savingsAccount);

    @Query(value = "SELECT * FROM Account WHERE userID = ?1 AND type = ?2", nativeQuery = true)
    Account getCorrectAccountByUserId(Long userId, boolean savingsAccount);

    @Query(value = "SELECT * FROM Account WHERE user_id = ?1 AND type = false", nativeQuery = true)
    Account getCurrentAccountByUserId(Integer userid);

    @Query(value = "SELECT * FROM Account WHERE user_id = ?1 AND type = true", nativeQuery = true)
    Account getSavingsAccountByUserId(Integer userid);
}
