package io.swagger.api.controller;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.api.AccountApi;
import io.swagger.model.*;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Controller
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


    public ResponseEntity<?> transaction(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount) {
        try {
            //get username of the current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Transaction transaction = accountService.PerformTransaction(1L, amount, targetIBAN);
            return ResponseEntity.status(200).body(transaction);
        } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    public ResponseEntity<?> deposit(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount) {
        try {
            //get username of the current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Deposit deposit = accountService.PerformDeposit(1l, amount, targetIBAN);
            return ResponseEntity.status(200).body(deposit);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    public ResponseEntity<?> withdrawal(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount) {
        try {
            //get username of the current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            Withdrawal withdrawal = accountService.PerformWithdrawal(1l, amount, targetIBAN);
            return ResponseEntity.status(200).body(withdrawal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

}
