package io.swagger.service;

import io.swagger.model.Deposit;
import io.swagger.model.User;
import io.swagger.model.Withdrawal;
import io.swagger.repository.UserRepository;
import io.swagger.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WithdrawalService {
    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private UserRepository userRepository;

    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    public void SaveWithdrawal(Withdrawal withdrawal) {
        withdrawalRepository.save(withdrawal);
    }

    public Withdrawal getWithdrawalById(Long withdrawalId) {
        return withdrawalRepository.getOne(withdrawalId);
    }

    public List<Withdrawal> getWithdrawalsByUser(Integer userId) {
        return withdrawalRepository.getWithdrawalsByUser(userId);
    }

    public List<Withdrawal> getWithdrawalHistory() {
        List<Withdrawal> withdrawals = withdrawalRepository.getWithdrawalsByUser(userRepository.getUserIdByUsername(authentication.getName()));
        if (!withdrawals.isEmpty()){
            return withdrawals;
        }
        else { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No withdrawals found"); }
    }
}
