package io.swagger.api;

import io.swagger.annotations.*;
import io.swagger.model.Deposit;
import io.swagger.model.Transaction;
import io.swagger.model.Withdrawal;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Validated
@Api(value = "Transaction", description = "the transaction API")
@RequestMapping(value = "/api")
public interface TransactionApi {
    @ApiOperation(value = "Deposit to savings account.", nickname = "deposit", notes = "", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transaction/deposit",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.POST)
    ResponseEntity<?> deposit(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount);


    @ApiOperation(value = "A transaction between two current accounts.", nickname = "transaction", notes = "", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transaction/transaction",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.POST)
    ResponseEntity<?> transaction(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount);


    @ApiOperation(value = "Withdraw from savings account.", nickname = "withdrawal", notes = "", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transaction/withdrawal",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.POST)
    ResponseEntity<?> withdrawal(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount);

    @ApiOperation(value = "Transaction history", nickname = "getTransactionHistory", notes = "This can only be done by the logged in user.", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transactions/transactions",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Transaction>> getTransactionHistory();

    @ApiOperation(value = "Deposit history", nickname = "getDepositHistory", notes = "This can only be done by the logged in user.", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transactions/deposits",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Deposit>> getDepositHistory();

    @ApiOperation(value = "Withdrawal history", nickname = "getWithdrawalHistory", notes = "This can only be done by the logged in user.", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transactions/withdrawals",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Withdrawal>> getWithdrawalHistory();

    @ApiOperation(value = "Transfer history", nickname = "getTransferHistory", notes = "This can only be done by the logged in user.", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transactions/transfers",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<?>> getTransferHistory();
}
