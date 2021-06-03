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

    @Query(value = "SELECT balance FROM Account WHERE iban = ?1 AND type = ?2")
    BigDecimal getBalanceByIban(String iban, boolean accountType);

    @Query(value = "UPDATE Account SET balance = ?1 WHERE iban = ?2 AND type = ?3")
    void UpdateBalance(BigDecimal newBalance, String iban, boolean accountType);

    @Query(value = "SELECT iban FROM Account WHERE iban = ?1")
    String getIban(String iban);

    @Query(value = "SELECT * FROM Account WHERE iban = ?1 AND type = ?2", nativeQuery = true)
    Account getCorrectAccountByIban(String iban, boolean savingsAccount);

    @Query(value = "SELECT * FROM Account WHERE userID = ?1 AND type = ?2", nativeQuery = true)
    Account getCorrectAccountByUserId(Integer userId, boolean savingsAccount);
}
