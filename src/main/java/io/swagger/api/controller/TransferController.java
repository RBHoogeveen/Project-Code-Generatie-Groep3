package io.swagger.api.controller;

import io.swagger.model.*;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class TransferController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    //method to perform the transaction
    public ResponseEntity<?> PerformTransaction(@RequestBody Long performerUserId, BigDecimal amount, String receiverIban) {
        try {
           return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //method to perform the deposit
    public ResponseEntity<?> PerformDeposit(@RequestBody Long performerUserId, BigDecimal amount, String receiverIban) {
        try {
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //method to perform the withdrawal
    public ResponseEntity<?> PerformWithdrawal(@RequestBody Long performerUserId, BigDecimal amount, String receiverIban) {
        try {
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
