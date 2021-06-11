package io.swagger.repository;

import io.swagger.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
    @Query(value = "SELECT * FROM Withdrawal WHERE user_performing_id = ?1", nativeQuery = true)
    List<Withdrawal> getWithdrawalsByUser(Integer userId);
}
