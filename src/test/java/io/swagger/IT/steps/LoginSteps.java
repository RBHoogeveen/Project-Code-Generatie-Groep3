package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.DTO.LoginDTO;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class LoginSteps {
  private HttpHeaders headers = new HttpHeaders();
  private String baseUrl = "http://localhost:8080/login/";
  private RestTemplate template = new RestTemplate();
  private ResponseEntity<String> responseEntity;

  @When("I successfully log in")
  public void iSuccessfullyLogIn() throws URISyntaxException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    LoginDTO dto = new LoginDTO();
    dto.setPassword("Admin");
    dto.setUsername("Admin");

    URI uri = new URI(baseUrl);
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(dto), headers);
    responseEntity = template.postForEntity(uri, entity, String.class);
  }

  @Then("I get http status {int} and a JWT token")
  public void iGetHttpStatusAndAJWTToken(int expected) {
    int response = responseEntity.getStatusCodeValue();
    Assert.assertEquals(expected, response);
  }
}
