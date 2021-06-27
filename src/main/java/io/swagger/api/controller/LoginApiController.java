package io.swagger.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.api.LoginApi;
import io.swagger.model.DTO.LoginDTO;
import io.swagger.model.DTO.LoginResponseDTO;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@RestController
public class LoginApiController implements LoginApi {

  private static final Logger log = LoggerFactory.getLogger(LoginApiController.class);

  private final ObjectMapper objectMapper;

  private final HttpServletRequest request;
  @Autowired
  private UserService userService;

  @org.springframework.beans.factory.annotation.Autowired
  public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request) {
    this.objectMapper = objectMapper;
    this.request = request;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@Parameter(in = ParameterIn.DEFAULT, description = "Request body to login a user", required = true, schema = @Schema()) @Valid @RequestBody LoginDTO body) {
    String token = userService.login(body.getUsername(), body.getPassword());
    LoginResponseDTO loginResponse = new LoginResponseDTO();
    loginResponse.setToken(token);
    return new ResponseEntity<LoginResponseDTO>(loginResponse, HttpStatus.OK);
  }

}
