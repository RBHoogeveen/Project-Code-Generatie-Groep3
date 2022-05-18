package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.Transaction;
import io.swagger.model.TransferType;
import io.swagger.repository.AccountRepository;
import io.swagger.service.TransactionService;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

public class TransactionSteps {
  private final String baseTransactionUrl = "http://localhost:8080/transaction/";
  private final HttpHeaders headers = new HttpHeaders();
  private final RestTemplate template = new RestTemplate();
  private final ObjectMapper mapper = new ObjectMapper();
  private ResponseEntity<String> responseEntity;
  private ResponseEntity<String> transactionResponse;
  private HttpEntity<String> entity;
  private HttpClient client;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private AccountRepository accountRepository;

  public TransactionSteps(HttpClient client) {
    this.client = client;
  }

  @And("get all transactions")
  public void getAllTransactions() throws Exception {
    client.getRequest(baseTransactionUrl, TransactionDTO.class);
  }

  @And("i get {int} transaction result(s)")
  public void iGetTransactionResult(int actual) throws Exception {
    int count = 1;
    if (count != actual)
      throw new Exception(String.format("Transaction count %d does not match expected %d", count, actual));
  }

  @When("I request transaction history of TYPE_TRANSACTION")
  public void iRequestTransactionHistoryOfTYPE_TRANSACTION() throws URISyntaxException {
    String baseUrl = "http://localhost:8080/transaction/history";
    URI uri = new URI(baseUrl);
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    responseEntity = template.getForEntity(uri, String.class);
  }

  @Then("I get {int} transaction of TYPE_TRANSACTION")
  public void iGetTransactionsOfTYPE_TRANSACTION(int expected) throws JSONException {
    String response = responseEntity.getBody();
    JSONArray actual = new JSONArray(response);
    Assert.assertEquals(expected, actual.length());
  }

  @And("I post a new transaction")
  public void iPostANewTransaction() throws URISyntaxException, JsonProcessingException {
    Transaction transaction = transactionService.makeTransaction(BigDecimal.valueOf(700), accountRepository.getAccountByIban("NL02INHO0000000002"), accountRepository.getAccountByIban("NL04INHO0000000004"), TransferType.TYPE_TRANSACTION);
    URI uri = new URI("http://localhost:8080/transactions/transaction");
    headers.setContentType(MediaType.APPLICATION_JSON);
    entity = new HttpEntity<>(mapper.writeValueAsString(transaction), headers);
    transactionResponse = template.postForEntity(uri, entity, String.class);
  }

  @When("I post a new deposit")
  public void iPostANewDeposit() throws URISyntaxException, JsonProcessingException {
    Transaction deposit = transactionService.makeTransaction(BigDecimal.valueOf(1000), accountRepository.getAccountByIban("NL02INHO0000000002"), accountRepository.getAccountByIban("NL03INHO0000000003"), TransferType.TYPE_DEPOSIT);
    URI uri = new URI("http://localhost:8080/transactions/deposit");
    headers.setContentType(MediaType.APPLICATION_JSON);
    entity = new HttpEntity<>(mapper.writeValueAsString(deposit), headers);
    transactionResponse = template.postForEntity(uri, entity, String.class);
  }

  @When("I post a new withdrawal")
  public void iPostANewWithdrawal() throws URISyntaxException, JsonProcessingException {
    Transaction withdrawal = transactionService.makeTransaction(BigDecimal.valueOf(2000), accountRepository.getAccountByIban("NL03INHO0000000003"), accountRepository.getAccountByIban("NL02INHO0000000002"), TransferType.TYPE_WITHDRAW);
    URI uri = new URI("http://localhost:8080/transactions/withdrawal");
    headers.setContentType(MediaType.APPLICATION_JSON);
    entity = new HttpEntity<>(mapper.writeValueAsString(withdrawal), headers);
    transactionResponse = template.postForEntity(uri, entity, String.class);
  }

  @Then("I get status {int}")
  public void iGetStatus(int expected) {
    int response = transactionResponse.getStatusCodeValue();
    Assert.assertEquals(expected, response);
  }
}
