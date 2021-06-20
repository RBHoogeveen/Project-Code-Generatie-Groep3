package io.swagger.repository;

import io.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "SELECT * FROM User WHERE username LIKE %?1%  ", nativeQuery = true)
    List<User> getUserBySearchterm(String searchterm);

    //todo * weghalen en de andere documentatie doen
    @Query(value = "SELECT * FROM User WHERE id = ?1", nativeQuery = true)
    User getUserById(Integer userId);

    @Query(value = "SELECT id FROM User WHERE username = ?1")
    Integer getUserIdByUsername(String username);
}
