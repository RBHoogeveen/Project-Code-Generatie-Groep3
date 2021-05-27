package io.swagger.api.temprepository;

import io.swagger.model.Account;
import org.iban4j.Iban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * FROM Account WHERE userID = ?1", nativeQuery = true)
    public Account getAccountByUserId(Long userId);

    public Account getAccountByIban(String iban);
}
