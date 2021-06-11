package io.swagger.api.controller;

import io.swagger.annotations.ApiParam;
import io.swagger.api.TransactionApi;
import io.swagger.model.DTO.DepositWithdrawalDTO;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.DTO.TransactionResponseDTO;
import io.swagger.model.Transaction;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@RestController
public class TransactionApiController implements TransactionApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionApiController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<TransactionResponseDTO> transaction(@ApiParam(value = "Enter target iban and an amount", required = true) @Valid @RequestBody TransactionDTO body) {
        try {
            TransactionResponseDTO transactionResponseDTO = transactionService.PerformTransaction(body);
            return new ResponseEntity<TransactionResponseDTO>(transactionResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<TransactionResponseDTO> deposit(@ApiParam(value = "Enter an amount to be transferred", required = true) @Valid @RequestBody DepositWithdrawalDTO body) {
        try {
            TransactionResponseDTO transactionResponseDTO = transactionService.PerformDeposit(body);
            return new ResponseEntity<TransactionResponseDTO>(transactionResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<TransactionResponseDTO> withdrawal(@ApiParam(value = "Enter an amount to be transferred", required = true) @Valid @RequestBody DepositWithdrawalDTO body) {
        try {
            TransactionResponseDTO transactionResponseDTO = transactionService.PerformWithdrawal(body);
            return new ResponseEntity<TransactionResponseDTO>(transactionResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Transaction>> getTransactionHistory() {
        List<Transaction> transactions = transactionService.getTransactionHistory();
        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<?>> getTransferHistory() {
        List transfers = accountService.getTransferHistory();
        return new ResponseEntity<List<?>>(transfers, HttpStatus.OK);
    }
}
