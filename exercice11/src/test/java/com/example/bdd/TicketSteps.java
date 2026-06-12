package com.example.bdd;

import com.example.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TicketSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions lastResult;
    private Long lastCreatedId;

    @Given("l'API ne contient aucun ticket")
    public void noTicketExists() {
        ticketRepository.deleteAll();
        lastCreatedId = null;
    }

    @And("un ticket avec le titre {string} et la priorité {string} a été créé")
    public void aTicketHasBeenCreated(String title, String priorityStr) throws Exception {
        String body = "{\"title\":\"" + title + "\",\"priority\":\"" + priorityStr + "\"}";
        ResultActions result = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
        String responseBody = result.andReturn().getResponse().getContentAsString();
        lastCreatedId = objectMapper.readTree(responseBody).get("id").asLong();
    }

    @And("un ticket résolu avec le titre {string} et la priorité {string} existe")
    public void aResolvedTicketExists(String title, String priorityStr) throws Exception {
        String createBody = "{\"title\":\"" + title + "\",\"priority\":\"" + priorityStr + "\"}";
        ResultActions createResult = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createBody));
        String responseBody = createResult.andReturn().getResponse().getContentAsString();
        lastCreatedId = objectMapper.readTree(responseBody).get("id").asLong();

        String statusBody = "{\"newStatus\":\"RESOLVED\"}";
        mockMvc.perform(patch("/api/tickets/" + lastCreatedId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(statusBody));
    }

    @When("je crée un ticket avec le titre {string} et la priorité {string}")
    public void createTicket(String title, String priorityStr) throws Exception {
        String body = "{\"title\":\"" + title + "\",\"priority\":\"" + priorityStr + "\"}";
        lastResult = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
        if (lastResult.andReturn().getResponse().getStatus() == 201) {
            String responseBody = lastResult.andReturn().getResponse().getContentAsString();
            lastCreatedId = objectMapper.readTree(responseBody).get("id").asLong();
        }
    }

    @When("je modifie le statut du ticket vers {string}")
    public void updateTicketStatus(String newStatus) throws Exception {
        String body = "{\"newStatus\":\"" + newStatus + "\"}";
        lastResult = mockMvc.perform(patch("/api/tickets/" + lastCreatedId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("je consulte le ticket avec l'identifiant {long}")
    public void getTicketById(Long id) throws Exception {
        lastResult = mockMvc.perform(get("/api/tickets/" + id));
    }

    @Then("la réponse HTTP doit être {int}")
    public void responseStatusShouldBe(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @And("la réponse contient le titre {string}")
    public void responseShouldContainTitle(String expectedTitle) throws Exception {
        lastResult.andExpect(jsonPath("$.title").value(expectedTitle));
    }

    @And("la réponse contient le statut {string}")
    public void responseShouldContainStatus(String expectedStatus) throws Exception {
        lastResult.andExpect(jsonPath("$.status").value(expectedStatus));
    }
}
