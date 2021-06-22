package io.swagger.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.api.AccountApi;
import io.swagger.model.Account;
import io.swagger.model.DTO.CreateUpdateAccountDTO;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
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
import javax.validation.constraints.NotNull;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@RestController
public class AccountApiController implements AccountApi {

    private static final Logger log = LoggerFactory.getLogger(AccountApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Account> updateAccount(@ApiParam(value = "Iban of account that needs to bee updated",required=true) @PathVariable("iban") String iban, @ApiParam(value = "Updated user object" ,required=true )  @Valid @RequestBody CreateUpdateAccountDTO body) {
        Account updatedAccount = accountService.updateAccount(body);

        return new ResponseEntity<Account>(updatedAccount, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Account> createAccount(@ApiParam(value = "Created account object" ,required=true )  @Valid @RequestBody CreateUpdateAccountDTO body) {
        Account createdAccount = accountService.add(body);
        return new ResponseEntity<Account>(createdAccount, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Account>> getUserAccount(@NotNull @ApiParam(value = "The username", required = true) @Valid @PathVariable(value = "username", required = true) String username) {
        List<Account> userAccounts = accountService.getUserAccounts(username);
        return new ResponseEntity<List<Account>>(userAccounts, HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
//    public ResponseEntity<Account> getAccountByIban(@NotNull @ApiParam(value = "The username", required = true) @Valid @PathVariable(value = "iban", required = true) String iban) {
//        Account account = accountService.getAccountByIban(iban);
//        return new ResponseEntity<Account>(account, HttpStatus.OK);
//    }
}
