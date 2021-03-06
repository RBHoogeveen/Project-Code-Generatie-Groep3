package io.swagger.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.api.UserApi;
import io.swagger.model.DTO.CreateUpdateUserDTO;
import io.swagger.model.User;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@RestController
public class UserApiController implements UserApi {

  private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

  private final ObjectMapper objectMapper;

  private final HttpServletRequest request;

  @Autowired
  private UserService userService;

  @Autowired
  private AccountService accountService;

  @org.springframework.beans.factory.annotation.Autowired
  public UserApiController(ObjectMapper objectMapper, HttpServletRequest request) {
    this.objectMapper = objectMapper;
    this.request = request;
  }

  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<User> createUser(@ApiParam(value = "Created user object", required = true) @Valid @RequestBody CreateUpdateUserDTO body) {
    User createdUser = userService.add(body);
    return new ResponseEntity<User>(createdUser, HttpStatus.OK);
  }

  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<User>> getListUsers() {
    String accept = request.getHeader("Accept");
    try {
      List<User> user = userService.getUsers();
      return ResponseEntity.status(200).body(user);
    } catch (Exception e) {
      return ResponseEntity.status(404).build();
    }
  }

  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<User>> getUserBySearchterm(@ApiParam(value = "The name that needs to be fetched. Use Admin for testing.", required = true) @PathVariable("searchterm") String searchTerm) {
    String accept = request.getHeader("Accept");
    try {
      List<User> user = userService.getUserBySearchterm(searchTerm);
      return ResponseEntity.status(200).body(user);
    } catch (Exception e) {
      return ResponseEntity.status(404).build();
    }
  }

  public ResponseEntity<User> updateUser(@ApiParam(value = "name that need to be updated", required = true) @PathVariable("username") String username, @ApiParam(value = "Updated user object", required = true) @Valid @RequestBody CreateUpdateUserDTO body) {
    User updatedUser = userService.updateUser(body);

    return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
  }
}
