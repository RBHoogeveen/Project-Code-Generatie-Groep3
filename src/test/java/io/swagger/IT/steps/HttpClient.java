package io.swagger.IT.steps;

import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


public class HttpClient {
    private HttpHeaders headers = new HttpHeaders();
    private int lastResponseCode;
    private String lastResponseErrorMsg;
    private ResponseEntity<?> lastResponse;
    private final RestTemplate restTemplate = new RestTemplate();


    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void matchLastResponse(int code) throws Exception {
        if (lastResponseCode != code)
            throw new Exception(String.format("Http code %d does not match the expected code %d", lastResponseCode, code));
    }

    public void matchLastResponseErrorMsg(String errorMessage) throws Exception {
        if (!lastResponseErrorMsg.equals(errorMessage)) {
            throw new Exception(String.format("Http message '%s' does not match the expected code  '%s'", lastResponseErrorMsg, errorMessage));
        }
    }

    public <T> ResponseEntity<T> getRequest(String uri, Class<T> target) throws Exception {
        return getRequest(new URI(uri), target);
    }

    public <T> ResponseEntity<T> getRequest(URI uri, Class<T> target) throws Exception {
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        return exchangeRequest(uri, target, entity, HttpMethod.GET);
    }

    public <T> ResponseEntity<T> postRequest(String uri, Class<T> target, Object o) throws Exception {
        return postRequest(new URI(uri), target, o);
    }

    public <T> ResponseEntity<T> postRequest(URI uri, Class<T> target, Object o) throws Exception {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(o, headers);
        return exchangeRequest(uri, target, entity, HttpMethod.POST);
    }

    public <T> ResponseEntity<T> putRequest(String uri, Class<T> target, Object o) throws Exception {
        return putRequest(new URI(uri), target, o);
    }

    public <T> ResponseEntity<T> putRequest(URI uri, Class<T> target, Object o) throws Exception {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(o, headers);
        return exchangeRequest(uri, target, entity, HttpMethod.PUT);
    }

    public <T> ResponseEntity<T> deleteRequest(String uri, Class<T> target, Object o) throws Exception {
        return deleteRequest(new URI(uri), target, o);
    }

    public <T> ResponseEntity<T> deleteRequest(URI uri, Class<T> target, Object o) throws Exception {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(o, headers);
        return exchangeRequest(uri, target, entity, HttpMethod.DELETE);
    }

    private <T> ResponseEntity<T> exchangeRequest(URI uri, Class<T> target, HttpEntity<Object> entity, HttpMethod method) throws Exception {
        try {
            ResponseEntity<T> response = restTemplate.exchange(uri, method, entity, target);
            lastResponse = response;
            lastResponseCode = response.getStatusCodeValue();

            return response;
        } catch (HttpStatusCodeException e) {
            lastResponse = null;
            lastResponseCode = e.getRawStatusCode();
            lastResponseErrorMsg = parseMessage(e.getResponseBodyAsString());

            return null;
        }
    }

    public String parseMessage(String message) throws Exception {
        if (message.equals(""))
            return "";

        JSONObject json = new JSONObject(message);
        if (json.has("message"))
            return json.getString("message");

        return "";
    }
}
