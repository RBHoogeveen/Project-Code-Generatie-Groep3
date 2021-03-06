package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.DTO.CreateUpdateUserDTO;
import io.swagger.model.Role;
import io.swagger.model.User;
import io.swagger.service.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

public class UserSteps {
  private final ObjectMapper mapper = new ObjectMapper();
  private HttpHeaders headers = new HttpHeaders();
  private String baseUrl = "http://localhost:8080/swagger-ui/#/users/";
  private RestTemplate template = new RestTemplate();
  private ResponseEntity<String> responseEntity;
  private ResponseEntity<User> userEntity;
  private HttpEntity<String> entity;

  @Autowired
  private UserService userService;

  private HttpClient client;

  public UserSteps(HttpClient client) {
    this.client = client;
  }

  @When("I retrieve all users")
  public void iRetrieveAllUsers() throws URISyntaxException {
    URI uri = new URI(baseUrl + "getListUsers");
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
    URI uri = new URI(baseUrl + "getListUsers");
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
  public void iCreateANewUser() throws URISyntaxException, JsonProcessingException {
    URI uri = new URI(baseUrl + "user");
    User user = userService.add(new CreateUpdateUserDTO("John2", "John", "Doe", "John@Doe.nl", "06-12121221", "Yo", BigDecimal.valueOf(1000), BigDecimal.valueOf(500), Collections.singletonList(Role.ROLE_USER), true, false, false));
    headers.setContentType(MediaType.APPLICATION_JSON);
    entity = new HttpEntity<>(mapper.writeValueAsString(user), headers);
    userEntity = template.postForEntity(uri, entity, User.class);
  }
}
