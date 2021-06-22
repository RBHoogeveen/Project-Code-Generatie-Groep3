package io.swagger.api;

import io.swagger.annotations.*;
import io.swagger.model.DTO.DepositWithdrawalDTO;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.DTO.TransactionResponseDTO;
import io.swagger.model.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Validated
@Api(value = "Transaction", description = "the transaction API")
@RequestMapping(value = "/api")
public interface TransactionApi {
    @ApiOperation(value = "Deposit to savings account.", nickname = "deposit", notes = "", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transactions/deposit",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.POST)
    ResponseEntity<TransactionResponseDTO> deposit(@ApiParam(value = "Enter an amount to be transferred", required = true) @Valid @RequestBody DepositWithdrawalDTO body);


    @ApiOperation(value = "A transaction between two current accounts.", nickname = "transaction", notes = "", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transactions/transaction",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.POST)
    ResponseEntity<TransactionResponseDTO> transaction(@ApiParam(value = "Enter target iban and an amount", required = true) @Valid @RequestBody TransactionDTO body);


    @ApiOperation(value = "Withdraw from savings account.", nickname = "withdrawal", notes = "", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transactions/withdrawal",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.POST)
    ResponseEntity<TransactionResponseDTO> withdrawal(@ApiParam(value = "Enter an amount to be transferred", required = true) @Valid @RequestBody DepositWithdrawalDTO body);

    @ApiOperation(value = "Transaction history", nickname = "getTransactionHistory", notes = "This can only be done by the logged in user.", tags = {"transaction",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/transactions",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Transaction>> getTransactionHistory();
}
