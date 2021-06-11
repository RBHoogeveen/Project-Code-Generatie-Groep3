package io.swagger.repository;

import io.swagger.model.Deposit;
import io.swagger.model.Transaction;
import io.swagger.model.Withdrawal;
import org.hibernate.type.IntegerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    List<Transaction> getAllByUserPerforming_Id(Integer userId);

    List<Transaction> getAllByUserPerforming_IdAndTransferType_TypeDeposit(Integer userId);

    List<Transaction> getAllByUserPerformingIdAndTransferType_TypeWithdraw(Integer userId);
}
