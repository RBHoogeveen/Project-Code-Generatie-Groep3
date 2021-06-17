package io.swagger.repository;

import io.swagger.model.Transaction;
import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    @Query(value = "SELECT * FROM Transaction WHERE user_performing_id = ?1", nativeQuery = true)
    List<Transaction> getTransactionsByUser(Integer userId);

    List<Transaction> getAllByUserPerforming(User userPerforming);

    @Query(value = "SELECT balance FROM Account WHERE iban = ?1 AND type = ?2")
    BigDecimal getBalanceByIban(String iban, boolean accountType);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account SET balance = ?1 WHERE iban = ?2 AND type = ?3", nativeQuery = true)
    void UpdateBalance(BigDecimal newBalance, String iban, boolean accountType);
}
