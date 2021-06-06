package io.swagger.IT.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class LoginSteps {
    private HttpHeaders headers = new HttpHeaders();
    private String baseUrl = "http://localhost:8080/api/login/";
    private RestTemplate template = new RestTemplate();
    private ResponseEntity<String> responseEntity;

    @When("I successfully log in")
    public void iSuccessfullyLogIn() {
    }

    @Then("I get http status {int} and a JWT token")
    public void iGetHttpStatusAndAJWTToken(int arg0) {
    }
}
