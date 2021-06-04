/**
 * NOTE: This class is auto generated by the swagger code generator program (2.4.19).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import java.math.BigDecimal;
import io.swagger.annotations.*;
import io.swagger.model.Account;
import io.swagger.model.DTO.CreateUpdateAccountDTO;
import io.swagger.model.User;
import io.swagger.model.Deposit;
import io.swagger.model.Transaction;
import io.swagger.model.Withdrawal;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Validated
@Api(value = "account", description = "the account API")
@RequestMapping(value = "/api")
public interface AccountApi {

    @ApiOperation(value = "Deposit to savings account.", nickname = "deposit", notes = "", tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation") })
    @RequestMapping(value = "/accounts/deposit",
        produces = { "application/xml", "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<?> deposit(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount);


    @ApiOperation(value = "A transaction between two current accounts.", nickname = "transaction", notes = "", tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation") })
    @RequestMapping(value = "/accounts/transaction",
        produces = { "application/xml", "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<?> transaction(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount);


    @ApiOperation(value = "Withdraw from savings account.", nickname = "withdrawal", notes = "", tags={ "account", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation") })
    @RequestMapping(value = "/accounts/withdrawal",
        produces = { "application/xml", "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<?> withdrawal(@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Target IBAN", required = true) String targetIBAN, @NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "Amount", required = true) BigDecimal amount);

    @ApiOperation(value = "Create account", nickname = "createAccount", notes = "This can only be done by the logged in user.", tags={ "account", })
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "successful operation") })
    @RequestMapping(value = "/account",
        produces = { "application/xml", "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<Account> createAccount(@ApiParam(value = "Created account object" ,required=true )  @Valid @RequestBody CreateUpdateAccountDTO body);

    @ApiOperation(value = "Updated account", nickname = "updateAccount", notes = "This can only be done by the logged in user.", tags={ "account", })
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid account supplied"),
        @ApiResponse(code = 404, message = "Account not found") })
    @RequestMapping(value = "/accounts/{iban}",
        produces = { "application/xml", "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<Account> updateAccount(@ApiParam(value = "Iban of the account that needs to be updated.",required=true) @PathVariable("iban") String username, @ApiParam(value = "Updated account object" ,required=true )  @Valid @RequestBody CreateUpdateAccountDTO body);
}
