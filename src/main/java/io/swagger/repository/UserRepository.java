package io.swagger.repository;

import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);
  
    @Query(value = "SELECT daySpent FROM User WHERE id = ?1")
    BigDecimal getDaySpent(Integer userId);

    @Query(value = "UPDATE User SET daySpent = ?1 WHERE id = ?2")
    void updateDaySpent(Integer userId, BigDecimal newDaySpent);

    @Query(value = "SELECT * FROM User", nativeQuery = true)
    List<User> getUsers();

    @Query(value = "SELECT * FROM User WHERE username LIKE %?1%  ", nativeQuery = true)
    List<User> getUserBySearchterm(String searchterm);

    @Query(value = "SELECT * FROM User WHERE id = ?1", nativeQuery = true)
    void getUserById(Integer userId);
}
