package io.swagger.api.controller;

import io.swagger.api.UserApi;
import io.swagger.model.Account;
import io.swagger.model.DTO.CreateUpdateUserDTO;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
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

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<User> createUser(@ApiParam(value = "Created user object" ,required=true )  @Valid @RequestBody CreateUpdateUserDTO body) {
        User createdUser = userService.add(body);
        return new ResponseEntity<User>(createdUser, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getListUsers() {
        String accept = request.getHeader("Accept");
        try{
           List<User> user = userService.getUsers();
            return ResponseEntity.status(200).body(user);
        }
        catch (Exception e){
            return ResponseEntity.status(404).build();
        }
    }

    @Override
    public ResponseEntity<?> getUserAccount(String username) {
        String accept = request.getHeader("Accept");
        try{
            List<Account> accounts = accountService.getUserAccountById(username);
            if (accounts.isEmpty()){
                return ResponseEntity.status(404).build();
            }
            else{
                return ResponseEntity.status(200).body(accounts);
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByName(@ApiParam(value = "The name that needs to be fetched. Use Admin for testing.",required=true) @PathVariable("username") String username) {
        String accept = request.getHeader("Accept");
        try{
            List<User> user = userService.getUserBySearchterm(username);
            if (user.isEmpty()){
                return ResponseEntity.status(404).build();
            }
            else{
                return ResponseEntity.status(200).body(user.get(0));
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public ResponseEntity<User> updateUser(@ApiParam(value = "name that need to be updated",required=true) @PathVariable("username") String username,@ApiParam(value = "Updated user object" ,required=true )  @Valid @RequestBody CreateUpdateUserDTO body) {
        User updatedUser = userService.updateUser(body);

        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
    }

}
