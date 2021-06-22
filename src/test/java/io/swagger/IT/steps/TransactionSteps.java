package io.swagger.IT.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class TransactionSteps {
    HttpHeaders headers = new HttpHeaders();
    RestTemplate template = new RestTemplate();
    ResponseEntity<String> responseEntity;

    @When("I request transaction history of TYPE_TRANSACTION")
    public void iRequestTransactionHistoryOfTYPE_TRANSACTION() throws URISyntaxException {
        String baseUrl = "http://localhost:8080/api/transactions";
        URI uri = new URI(baseUrl);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        responseEntity = template.getForEntity(uri, String.class);
    }

    @Then("I get {int} transactions of TYPE_TRANSACTION")
    public void iGetTransactionsOfTYPE_TRANSACTION(int expected) throws JSONException {
        String response = responseEntity.getBody();
        JSONArray actual = new JSONArray(response);
        Assert.assertEquals(expected, actual.length());
    }
}
