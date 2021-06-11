package io.swagger.repository;

import io.swagger.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
  List<Withdrawal> getAllByUserPerforming_Id(Integer userId);
}
