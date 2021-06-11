package io.swagger.repository;

import io.swagger.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
    @Query(value = "SELECT * FROM Deposit WHERE user_performing_id = ?1", nativeQuery = true)
    List<Deposit> getDepositsByUser(Integer userId);
}
