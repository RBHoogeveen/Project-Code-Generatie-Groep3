package io.swagger.service;

import io.swagger.model.DTO.CreateUpdateUserDTO;
import io.swagger.model.User;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.regex.Pattern;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

  @Autowired
  AccountService accountService;

  @Autowired
  JwtTokenProvider jwtTokenProvider;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  PasswordEncoder passwordEncoder;

    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.findByUsername(username);
            String token = jwtTokenProvider.createToken(username, user.getRoles());
            return token;
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username/password invalid");
        }
    }

  public User add(CreateUpdateUserDTO createUpdateUser) {
    try {
      if (userRepository.findByUsername(createUpdateUser.getUsername()) == null) {
        User user = new User();
        user.setUsername(createUpdateUser.getUsername());
        if (createUpdateUser.getFirstname().chars().allMatch(Character::isLetter)) {
          user.setFirstname(createUpdateUser.getFirstname());
        } else {
          throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Firstname can only contain letters.");
        }
        if (createUpdateUser.getLastname().chars().allMatch(Character::isLetter)) {
          user.setLastname(createUpdateUser.getLastname());
        } else {
          throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Lastname can only contain letters.");
        }
        user.setEmail(createUpdateUser.getEmail());
        user.setPhonenumber(createUpdateUser.getPhonenumber());
        if (createUpdateUser.getDayLimit().compareTo(BigDecimal.ZERO) >= 0) {
          user.setDayLimit(createUpdateUser.getDayLimit());
        } else {
          throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Day limit can't be lower than 0");
        }
        if (createUpdateUser.getTransactionLimit().compareTo(BigDecimal.ZERO) >= 0) {
          user.setTransactionLimit(createUpdateUser.getTransactionLimit());
        } else {
          throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Transaction limit can't be lower than 0");
        }
        user.setRoles(createUpdateUser.getRoles());
        user.setPassword(passwordEncoder.encode(createUpdateUser.getPassword()));
        user.setIsActive(createUpdateUser.getIsActive());
        user.setDaySpent(BigDecimal.ZERO);
        userRepository.save(user);
        if (createUpdateUser.getCreateCurrentAccount()) {
          accountService.createCurrentAccount(createUpdateUser.getUsername());
        }
        if (createUpdateUser.getCreateSavingsAccount()) {
          accountService.createSavingsAccount(createUpdateUser.getUsername());
        }
        return user;
      } else {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username already in use");
      }
    } catch (ResponseStatusException e) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }
  }

    public User updateUser (CreateUpdateUserDTO createUpdateUser){
      try {
        User updatedUser = userRepository.findByUsername(createUpdateUser.getUsername());
        updatedUser.setUsername(createUpdateUser.getUsername());
        if (Pattern.matches("[a-zA-Z]+", createUpdateUser.getFirstname())) {
          updatedUser.setFirstname(createUpdateUser.getFirstname());
        } else {
          throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Firstname can only contain letters.");
        }
        if (Pattern.matches("[a-zA-Z]+", createUpdateUser.getLastname())) {
          updatedUser.setLastname(createUpdateUser.getLastname());
        } else {
          throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Lastname can only contain letters.");
        }
        updatedUser.setEmail(createUpdateUser.getEmail());
        updatedUser.setPhonenumber(createUpdateUser.getPhonenumber());
        if (createUpdateUser.getDayLimit().compareTo(BigDecimal.ZERO) >= 0) {
          updatedUser.setDayLimit(createUpdateUser.getDayLimit());
        } else {
          throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Day limit can't be lower than 0");
        }
        if (createUpdateUser.getTransactionLimit().compareTo(BigDecimal.ZERO) >= 0) {
          updatedUser.setTransactionLimit(createUpdateUser.getTransactionLimit());
        } else {
          throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Transaction limit can't be lower than 0");
        }
        updatedUser.setRoles(createUpdateUser.getRoles());
        updatedUser.setPassword(passwordEncoder.encode(createUpdateUser.getPassword()));
        updatedUser.setIsActive(createUpdateUser.getIsActive());
        userRepository.save(updatedUser);
        return updatedUser;
      } catch (ResponseStatusException e) {
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
      }
    }

    public User getUserById (Long userId){
      return userRepository.getOne(userId);
    }

    public BigDecimal getDaySpent (Integer userId){
      return userRepository.getDaySpent(userId);
    }

    public void updateDaySpent (Integer userId, BigDecimal newDaySpent){
      userRepository.updateDaySpent(newDaySpent, userId);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<User> getUserBySearchterm(String searchterm) {
        return userRepository.getUserBySearchterm(searchterm);
    }

    public User getUserByUsername(String username) {
      return userRepository.getUserByUsername(username);
    }
}

