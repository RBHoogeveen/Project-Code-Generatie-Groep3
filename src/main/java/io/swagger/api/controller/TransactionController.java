package io.swagger.api.controller;

import io.swagger.model.User;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.model.Account;
import io.swagger.model.Transaction;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/transaction", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveTransaction(@RequestBody Transaction transaction) {
        try {
            transactionService.SaveTransaction(transaction);
            return ResponseEntity.status(200).body(transaction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //TODO if statements toevoegen voor checks absolute limit, daylimit en transaction limt OFWEL JE MOET USER OOK HEBBEN
    //method to perform the transaction
    public ResponseEntity<Account> PerformTransaction(@RequestBody Long performerUserId, BigDecimal amount, String receiverIban) {
        try {
            //get the account performing that wants to perform the transaction
            Account performerAccount = accountService.getUserAccountById(performerUserId);

            //get the performer user (maybe user will be the parameter instead of userid in the future)
            User performerUser = userService.getUserById(performerUserId);

            //get the receiver account
            Account receiverAccount = accountService.getUserAccountByIban(receiverIban);

            //make the transaction
            Transaction transaction = performerAccount.MakeTransaction(amount, receiverAccount, performerAccount);

            //subtract amount from performer and add to receiver
            //TODO code om bedrag over te maken

            //save the transaction
            transactionService.SaveTransaction(transaction);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
