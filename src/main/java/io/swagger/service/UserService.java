package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.DTO.CreateUpdateUserDTO;
import io.swagger.model.Role;
import io.swagger.model.User;
import io.swagger.repository.AccountRepository;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  AccountService accountService;

  @Autowired
  AccountRepository accountRepository;

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

  //TODO fix nullpointers
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
        if (!accountRepository.getCurrentAccountByUserId(updatedUser.getId()).getIban().equals("NL01INHO0000000001")){
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
        }
        else { throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Not allowed to update Banks account.");}
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
      userRepository.updateDaySpent(userId, newDaySpent);
    }

  public void createBasicUsers() {
    CreateUpdateUserDTO bank = new CreateUpdateUserDTO();
    bank.setCreateCurrentAccount(false);
    bank.setCreateSavingsAccount(false);
    bank.setIsActive(true);
    bank.setEmail("Bank@bank.nl");
    bank.setFirstname("Bank");
    bank.setLastname("Bank");
    bank.setDayLimit(BigDecimal.valueOf(10000000));
    bank.setPhonenumber("06-12121212");
    bank.setRoles(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
    bank.setTransactionLimit(BigDecimal.valueOf(10000000));
    bank.setUsername("Bank");
    bank.setPassword("Bank");
    add(bank);

    Account bankAccount = new Account();
    bankAccount.setUser(userRepository.findByUsername(bank.getUsername()));
    bankAccount.setBalance(BigDecimal.valueOf(999999999));
    bankAccount.setIban("NL01INHO0000000001");
    bankAccount.setIsActive(true);
    bankAccount.setAbsoluteLimit(BigDecimal.ZERO);
    bankAccount.setType(false);
    accountRepository.save(bankAccount);


    CreateUpdateUserDTO admin = new CreateUpdateUserDTO();
    admin.setCreateCurrentAccount(true);
    admin.setCreateSavingsAccount(true);
    admin.setIsActive(true);
    admin.setEmail("Admin@admin.nl");
    admin.setFirstname("Admin");
    admin.setLastname("Admin");
    admin.setDayLimit(BigDecimal.valueOf(12345));
    admin.setPhonenumber("06-12345678");
    admin.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
    admin.setTransactionLimit(BigDecimal.valueOf(5000));
    admin.setUsername("Admin");
    admin.setPassword("Admin");
    add(admin);

    CreateUpdateUserDTO user = new CreateUpdateUserDTO();
    user.setCreateCurrentAccount(true);
    user.setCreateSavingsAccount(true);
    user.setIsActive(true);
    user.setEmail("User@user.nl");
    user.setFirstname("User");
    user.setLastname("User");
    user.setDayLimit(BigDecimal.valueOf(54321));
    user.setPhonenumber("06-34343434");
    user.setRoles(Collections.singletonList(Role.ROLE_USER));
    user.setTransactionLimit(BigDecimal.valueOf(3000));
    user.setUsername("User");
    user.setPassword("User");
    add(user);
  }
}

