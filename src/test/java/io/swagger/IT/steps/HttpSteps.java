package io.swagger.IT.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class HttpSteps {
    private HttpClient client;

    public HttpSteps(HttpClient client){
        this.client = client;
    }

    @Then("i get http code {int}")
    public void iGetHttpStatus(int actual) throws Exception {
        client.matchLastResponse(actual);
    }

    @And("Http message equals {string}")
    public void httpMessageEquals(String errorMessage) throws Exception {
        client.matchLastResponseErrorMsg(errorMessage);
    }
}
