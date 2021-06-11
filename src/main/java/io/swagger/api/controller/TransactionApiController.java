package io.swagger.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.api.TransactionApi;
import io.swagger.model.Deposit;
import io.swagger.model.Transaction;
import io.swagger.model.Withdrawal;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@RestController
public class TransactionApiController implements TransactionApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionApiController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private WithdrawalService withdrawalService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> transaction(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount) {
        try {
            //get username of the current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            //perform the transaction
            Transaction transaction = accountService.PerformTransaction(username, amount, targetIBAN);
            return ResponseEntity.status(201).body(transaction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> deposit(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount) {
        try {
            //get username of the current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            //perform the deposit
            Deposit deposit = accountService.PerformDeposit(username, amount, targetIBAN);
            return ResponseEntity.status(201).body(deposit);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> withdrawal(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount) {
        try {
            //get username of the current user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            //perform the withdrawal
            Withdrawal withdrawal = accountService.PerformWithdrawal(username, amount, targetIBAN);
            return ResponseEntity.status(201).body(withdrawal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Transaction>> getTransactionHistory() {
        List<Transaction> transactions = transactionService.getTransactionHistory();
        return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Deposit>> getDepositHistory() {
        List<Deposit> deposits = depositService.getDepositHistory();
        return new ResponseEntity<List<Deposit>>(deposits, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory() {
        List<Withdrawal> withdrawals = withdrawalService.getWithdrawalHistory();
        return new ResponseEntity<List<Withdrawal>>(withdrawals, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<?>> getTransferHistory() {
        List transfers = accountService.getTransferHistory();
        return new ResponseEntity<List<?>>(transfers, HttpStatus.OK);
    }
}
