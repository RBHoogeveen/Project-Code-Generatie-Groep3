package io.swagger.repository;

import io.swagger.model.Transaction;
import io.swagger.model.Transfer;
import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    @Query(value = "SELECT * FROM Transaction WHERE user_performing_id = ?1", nativeQuery = true)
    List<Transaction> getTransactionsByUser(Integer userId);
}
