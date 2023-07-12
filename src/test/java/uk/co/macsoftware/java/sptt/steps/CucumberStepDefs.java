package uk.co.macsoftware.java.sptt.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import uk.co.macsoftware.java.sptt.model.ReadResponse;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@Sql(scripts = "/data/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CucumberStepDefs {

    @LocalServerPort
    String port;

    ResponseEntity<String> response;

    String requestBody;

    @Given("the request body is")
    public void theRequestBodyIs(String requestBody) {
        this.requestBody = requestBody;
    }

    @When("the client calls endpoint {string} with credentials {string}, {string}")
    public void whenClientCalls(String endpoint, String username, String password) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, password);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            response = new RestTemplate().exchange("http://localhost:" + port + endpoint, HttpMethod.GET, entity, String.class);
        }catch (HttpClientErrorException e) {
            response = ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @Then("response status code is {int}")
    public void thenStatusCode(int statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }

    @Then("returned string should be {string}")
    public void thenStringIs(String expectedString) {
        assertThat(expectedString, response.getBody().contains(expectedString));
    }

    @When("the client calls endpoint {string} with account number {int} and credentials {string}, {string}")
    public void whenClientCallsEndpointWithAccountNumber(String endpoint, int accountNum, String username, String password) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, password);
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            response = new RestTemplate().exchange("http://localhost:" + port + endpoint + accountNum, HttpMethod.GET, entity, String.class);
        }catch (HttpClientErrorException e) {
            response = ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @And("returned read response is valid with account number {long}, {int} gas readings and {int} elec readings")
    public void returnedReadResponseIsValid(Long accountNumber, int gasReadings, int elecReadings) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ReadResponse readResponse = mapper.readValue(response.getBody(), ReadResponse.class);

        assertEquals(accountNumber, readResponse.getAccountId());
        assertEquals(gasReadings, readResponse.getGasReadings().size());
        assertEquals(elecReadings, readResponse.getElecReadings().size());

    }

    @When("the client calls endpoint {string} with valid body and credentials {string}, {string}")
    public void theClientCallsEndpointWithValidBody(String endpoint, String username, String password) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, password);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(this.requestBody, headers);

            response = new RestTemplate().exchange("http://localhost:" + port + endpoint, HttpMethod.POST, entity, String.class);
        }catch (HttpClientErrorException e) {
            response = ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @And("response body contains {string}")
    public void responseBodyContains(String stringContains) {
        assertThat(response.getBody(), response.getBody().contains(stringContains));
    }
}
