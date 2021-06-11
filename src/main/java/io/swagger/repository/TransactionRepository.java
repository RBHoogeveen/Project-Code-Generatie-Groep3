package io.swagger.repository;

import io.swagger.model.Deposit;
import io.swagger.model.Transaction;
import io.swagger.model.Withdrawal;
import org.hibernate.type.IntegerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

    @Query(value = "SELECT balance FROM Account WHERE iban = ?1 AND type = ?2")
    BigDecimal getBalanceByIban(String iban, boolean accountType);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account SET balance = ?1 WHERE iban = ?2 AND type = ?3", nativeQuery = true)
    void UpdateBalance(BigDecimal newBalance, String iban, boolean accountType);

    List<Transaction> getAllByUserPerforming_Id(Integer userId);

    List<Transaction> getAllByUserPerforming_IdAndTransferType_TypeDeposit(Integer userId);

    List<Transaction> getAllByUserPerformingIdAndTransferType_TypeWithdraw(Integer userId);
}
