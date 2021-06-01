package io.swagger.service;

import io.swagger.model.User;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.getOne(userId);
    }

    public BigDecimal getDaySpent(Integer userId) {
        return userRepository.getDaySpent(userId);
    }

    public void updateDaySpent(Integer userId, BigDecimal newDaySpent) {
        userRepository.updateDaySpent(userId, newDaySpent);
    }
}
