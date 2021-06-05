package io.swagger.service;

import io.swagger.model.User;
import io.swagger.model.Withdrawal;
import io.swagger.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WithdrawalService {
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    public void SaveWithdrawal(Withdrawal withdrawal) {
        withdrawalRepository.save(withdrawal);
    }

    public Withdrawal getWithdrawalById(Long withdrawalId) {
        return withdrawalRepository.getOne(withdrawalId);
    }

    public List<Withdrawal> getWithdrawalsByUser(Integer userId) {
        return withdrawalRepository.getWithdrawalsByUser(userId);
    }
}
