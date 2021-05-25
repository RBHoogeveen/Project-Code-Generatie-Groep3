package io.swagger.api.controller;

import io.swagger.api.TransactionService;
import io.swagger.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;


    @RequestMapping(value = "/transaction", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveTransaction(@RequestBody Transaction transaction) {
        try {
            transactionService.SaveTransaction(transaction);
            return ResponseEntity.status(200).body(transaction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
