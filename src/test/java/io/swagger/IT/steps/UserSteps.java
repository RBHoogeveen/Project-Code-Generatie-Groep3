package io.swagger.IT.steps;

import io.cucumber.java.bs.A;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.User;
import io.swagger.repository.UserRepository;
import io.swagger.service.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class UserSteps {

  private HttpHeaders headers = new HttpHeaders();
  private String baseUrl = "http://localhost:8080/users/"; //andere url?
  private RestTemplate template = new RestTemplate();
  private ResponseEntity<String> responseEntity;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;

  @When("I retrieve all users")
  public void iRetrieveAllUsers() throws URISyntaxException {
    URI uri =  new URI(baseUrl);
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    responseEntity = template.getForEntity(uri, String.class);
  }

  @Then("I get http status {int}")
  public void iGetHttpStatus(int expected) {
    int response = responseEntity.getStatusCodeValue();
    Assert.assertEquals(expected, response);
  }


  @When("I fetch all users")
  public void iFetchAllUsers() throws URISyntaxException {
    URI uri =  new URI(baseUrl);
    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    responseEntity = template.getForEntity(uri, String.class);
  }

  @Then("I get a list of {int} users")
  public void iGetAListOfMultipleUsers(int expected) throws JSONException {
    String response = responseEntity.getBody();
    JSONArray jsonArray = new JSONArray(response);
    int actual = jsonArray.length();
    Assert.assertEquals(expected, actual);
  }

  @When("I retrieve a user with searchTerm {string}")
  public void iRetrieveAUserWithSearchTerm(String searchterm) throws URISyntaxException {
    URI uri = new URI(baseUrl + searchterm);
    responseEntity = template.getForEntity(uri, String.class);
  }

  @Then("I get a dayLimit with {int}")
  public void iGetADayLimitWith(int expected) throws JSONException {
    String response = responseEntity.getBody();
    JSONObject actual = new JSONObject(response);
    Assert.assertEquals(expected, actual.getInt("dayLimit"));
  }

  @When("I create a new user")
  public void iCreateANewUser() {

  }

  @Then("I get the created user")
  public void iGetTheCreatedUser() {

  }
}
