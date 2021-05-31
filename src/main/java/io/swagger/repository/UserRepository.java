package io.swagger.repository;

import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT daySpent FROM User WHERE id = ?1")
    BigDecimal getDaySpent(Integer userId);

    @Query(value = "UPDATE User SET daySpent = ?1 WHERE id = ?2")
    void updateDaySpent(Integer userId, BigDecimal newDaySpent);
}