package io.swagger.service;

import io.swagger.model.Deposit;
import io.swagger.model.User;
import io.swagger.repository.DepositRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DepositService {
    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private UserRepository userRepository;

    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    public void SaveDeposit(Deposit deposit) {
        depositRepository.save(deposit);
    }

    public Deposit getDepositById(Long depositId) {
        return depositRepository.getOne(depositId);
    }

    public List<Deposit> getDepositsByUser(Integer userId) {
        return depositRepository.getDepositsByUser(userId);
    }

    public List<Deposit> getDepositHistory() {
        List<Deposit> deposits = depositRepository.getDepositsByUser(userRepository.getUserIdByUsername(authentication.getName()));
        if (!deposits.isEmpty()){
            return deposits;
        }
        else { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No deposits found"); }
    }
}
