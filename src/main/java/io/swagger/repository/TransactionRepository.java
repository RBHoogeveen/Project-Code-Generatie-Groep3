package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  @Transactional
  @Modifying
  @Query(value = "UPDATE Account SET balance = ?1 WHERE iban = ?2 AND type = ?3", nativeQuery = true)
  void UpdateBalance(BigDecimal newBalance, String iban, boolean accountType);

  List<Transaction> getAllByUserPerforming_Id(Integer userId);

  @Query(value = "SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.date = CURRENT_DATE AND t.userPerforming.id = ?1")
  BigDecimal getTotalAmountOfTransactionsByUserId(Integer userId);
}
