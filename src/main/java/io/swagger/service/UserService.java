package io.swagger.service;

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

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

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

  public User add(User user) {
    if (userRepository.findByUsername(user.getUsername()) == null){
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
      return user;
    }
    else {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username already in use");
    }
  }
}
