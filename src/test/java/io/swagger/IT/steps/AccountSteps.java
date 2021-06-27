package io.swagger.IT.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class AccountSteps {
  private HttpHeaders headers = new HttpHeaders();
  private String baseUrl = "http://localhost:8080/accounts/";
  private RestTemplate template = new RestTemplate();
  private ResponseEntity<String> responseEntity;

  @When("I get accounts with {string}")
  public void IGetAccountsWith(String username) throws URISyntaxException {
    URI uri = new URI(baseUrl + username);
    responseEntity = template.getForEntity(uri, String.class);
  }

  @When("I put the account with iban {string}")
  public void iPutTheAccountWithIban(String arg0) {

  }

  @When("I create an account")
  public void iCreateAnAccount() {
  }
}
