package io.swagger.api.controller;

import io.swagger.api.UserApi;
import io.swagger.model.DTO.CreateUpdateUserDTO;
import io.swagger.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Controller
public class UserApiController implements UserApi {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UserApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Autowired
    UserService userService = new UserService();

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@ApiParam(value = "Created user object" ,required=true )  @Valid @RequestBody CreateUpdateUserDTO body) {
        User createdUser = userService.add(body);
        return new ResponseEntity<User>(createdUser, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> getListUsers() {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> getUserAccount(@ApiParam(value = "",required=true) @PathVariable("username") String username) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByName(@ApiParam(value = "The name that needs to be fetched. Use user1 for testing.",required=true) @PathVariable("username") String username) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/xml")) {
            try {
                return new ResponseEntity<User>(objectMapper.readValue("<null>  <id>123</id>  <username>aeiou</username>  <firstname>aeiou</firstname>  <lastname>aeiou</lastname>  <email>aeiou</email>  <phonenumber>aeiou</phonenumber>  <password>MySecretPassword</password>  <isCustomer>true</isCustomer>  <isEmployee>true</isEmployee>  <absoluteLimit>1.3579</absoluteLimit>  <dayLimit>1.3579</dayLimit>  <transactionLimit>1.3579</transactionLimit></null>", User.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/xml", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<User>(objectMapper.readValue("{  \"isCustomer\" : true,  \"firstname\" : \"firstname\",  \"password\" : \"MySecretPassword\",  \"isEmployee\" : true,  \"dayLimit\" : 1.46581298050294517310021547018550336360931396484375,  \"absoluteLimit\" : 6.02745618307040320615897144307382404804229736328125,  \"phonenumber\" : \"phonenumber\",  \"id\" : 0,  \"transactionLimit\" : 5.962133916683182377482808078639209270477294921875,  \"email\" : \"email\",  \"username\" : \"username\",  \"lastname\" : \"lastname\"}", User.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<User> updateUser(@ApiParam(value = "name that need to be updated",required=true) @PathVariable("username") String username,@ApiParam(value = "Updated user object" ,required=true )  @Valid @RequestBody CreateUpdateUserDTO body) {
        User updatedUser = userService.updateUser(body);

        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
    }

}
