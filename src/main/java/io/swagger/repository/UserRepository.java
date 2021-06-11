package io.swagger.repository;

import io.swagger.model.Account;
import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE User SET day_spent = ?1 WHERE id = ?2", nativeQuery = true)
    void updateDaySpent(BigDecimal newDaySpent, Integer userId);

    @Query(value = "SELECT * FROM User", nativeQuery = true)
    List<User> getUsers();

    @Query(value = "SELECT * FROM User WHERE username LIKE %?1%  ", nativeQuery = true)
    List<User> getUserBySearchterm(String searchterm);

    @Query(value = "SELECT * FROM User WHERE id = ?1", nativeQuery = true)
    User getUserById(Integer userId);

    @Query(value = "SELECT ID FROM User WHERE username = '?1'", nativeQuery = true)
    Integer getUserIdByUsername(String username);
}
