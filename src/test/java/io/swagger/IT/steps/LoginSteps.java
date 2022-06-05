package io.swagger.IT.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.IT.steps.Models.LoginDTO;
import io.swagger.IT.steps.Models.LoginResponseDTO;
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
  private String baseUrl = "http://localhost:8080/login";
  private RestTemplate template = new RestTemplate();
  private ResponseEntity<String> responseEntity;
  private HttpClient client;

  public LoginSteps(HttpClient client) {
    this.client = client;
  }

  @When("i log in with username {string} and password {string}")
  public void iLogInWithUsernameAndPassword(String arg0, String arg1) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    LoginDTO login = new LoginDTO(arg0, arg1);

    ResponseEntity<LoginResponseDTO> loginResponse = client.postRequest(baseUrl, LoginResponseDTO.class, login);

    client.matchLastResponse(200);
    headers.add("Authorization", "Bearer " + loginResponse.getBody().getToken());
    client.setHeaders(headers);
  }
}
