package io.swagger.service;

import io.swagger.model.Deposit;
import io.swagger.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositService {
    @Autowired
    private DepositRepository depositRepository;

    public void SaveDeposit(Deposit deposit) {
        depositRepository.save(deposit);
    }

    public Deposit getDepositById(Long depositId) {
        return depositRepository.getOne(depositId);
    }

    public List<Deposit> getDepositsByUser(Integer userId) {
        return depositRepository.getDepositsByUser(userId);
    }
}
