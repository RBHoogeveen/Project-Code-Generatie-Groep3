package io.swagger.IT.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class UserSteps {

  private HttpHeaders headers = new HttpHeaders();
  private String baseUrl = "http://localhost:8080/swagger-ui/#/user/getListUsers/";
  private String searchTermUrl = "http://localhost:8080/swagger-ui/#/user/getUserByName/";
  private RestTemplate template = new RestTemplate();
  private ResponseEntity<String> responseEntity;

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
    URI uri = new URI(searchTermUrl + searchterm);
    responseEntity = template.getForEntity(uri, String.class);
  }

  @Then("I get a dayLimit with {int}")
  public void iGetADayLimitWith(int expected) throws JSONException {
    String response = responseEntity.getBody();
    JSONObject actual = new JSONObject(response);
    Assert.assertEquals(expected, actual.getInt("dayLimit"));
  }
}
