/**
 * NOTE: This class is auto generated by the swagger code generator program (2.4.19).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.annotations.*;
import io.swagger.model.Account;
import io.swagger.model.DTO.CreateUpdateAccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-05-21T11:36:55.738Z")

@Validated
@Api(value = "account", description = "the account API")
@RequestMapping(value = "/api")
public interface AccountApi {

    @ApiOperation(value = "Create account", nickname = "createAccount", notes = "This can only be done by the logged in user.", tags = {"account",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "successful operation")})
    @RequestMapping(value = "/accounts",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.POST)
    ResponseEntity<Account> createAccount(@ApiParam(value = "Created account object", required = true) @Valid @RequestBody CreateUpdateAccountDTO body);

    @ApiOperation(value = "Updated account", nickname = "updateAccount", notes = "This can only be done by the logged in user.", tags = {"account",})
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid iban supplied"),
            @ApiResponse(code = 404, message = "Account not found")})
    @RequestMapping(value = "/accounts/{iban}",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<Account> updateAccount(@ApiParam(value = "Iban of the account that needs to be updated.", required = true) @PathVariable("iban") String username, @ApiParam(value = "Updated account object", required = true) @Valid @RequestBody CreateUpdateAccountDTO body);

    @ApiOperation(value = "Get users account", nickname = "getUserAccount", notes = "Get the accounts of the given username", tags = {"account",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = Account.class, responseContainer = "List")})
    @RequestMapping(value = "/accounts/{username}",
            produces = {"application/xml", "application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<Account>> getUserAccount(@ApiParam(value = "The username", required = true) @PathVariable("username") String username);

//    @ApiOperation(value = "Get account by iban", nickname = "getAccountByIban", notes = "Get the accounts of the given iban", tags = {"account",})
//    @ApiResponses(value = {
//        @ApiResponse(code = 200, message = "successful operation", response = Account.class, responseContainer = "List")})
//    @RequestMapping(value = "/accounts/{iban}",
//        produces = {"application/xml", "application/json"},
//        method = RequestMethod.GET)
//    ResponseEntity<Account> getAccountByIban(@ApiParam(value = "The Iban", required = true) @PathVariable("iban") String Iban);
}
