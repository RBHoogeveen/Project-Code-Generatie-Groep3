package io.swagger.api.controller;

import io.swagger.model.DTO.LoginDTO;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//TODO beter maken met exceptions

@RestController
public class LoginController {

  @Autowired
  UserService userService;

  @PostMapping("/login")
  public String login(@RequestBody LoginDTO login){
    return userService.login(login.getUsername(), login.getPassword());
  }
}
