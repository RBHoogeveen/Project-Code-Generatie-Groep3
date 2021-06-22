package io.swagger.repository;

import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "SELECT u FROM User u WHERE u.username LIKE %?1%")
    List<User> getUserBySearchterm(String searchterm);

    @Query(value = "SELECT id FROM User WHERE username = ?1")
    Integer getUserIdByUsername(String username);
}
