package io.swagger.repository;

import io.swagger.model.Deposit;
import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
    @Query(value = "SELECT * FROM Deposit WHERE userID = ?1", nativeQuery = true)
    List<Deposit> getDepositsByUser(User user);
}
