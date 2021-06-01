package io.swagger.repository;

import io.swagger.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    @Query(value = "SELECT * FROM Transaction WHERE userPerforming = ?1", nativeQuery = true)
    List<Transaction> getTransactionsByUser(Integer userId);
}