package com.example.bdd;

import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions lastResult;
    private Long lastCreatedRoomId;
    private Long lastCreatedReservationId;

    @Given("les données sont réinitialisées")
    public void resetData() {
        reservationRepository.deleteAll();
        roomRepository.deleteAll();
        lastCreatedRoomId = null;
        lastCreatedReservationId = null;
    }

    @Given("une salle {string} de capacité {int} a été créée")
    public void createRoom(String name, int capacity) throws Exception {
        String body = "{\"name\":\"" + name + "\",\"capacity\":" + capacity + "}";
        String response = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn().getResponse().getContentAsString();
        lastCreatedRoomId = objectMapper.readTree(response).get("id").asLong();
    }

    @And("une réservation a été faite pour {string} du {string} au {string}")
    public void aReservationExists(String personName, String start, String end) throws Exception {
        String body = buildReservationBody(lastCreatedRoomId, personName, start, end);
        String response = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn().getResponse().getContentAsString();
        lastCreatedReservationId = objectMapper.readTree(response).get("id").asLong();
    }

    @And("cette réservation a été annulée")
    public void cancelCurrentReservation() throws Exception {
        mockMvc.perform(delete("/api/reservations/" + lastCreatedReservationId + "/cancel"));
    }

    @When("je réserve cette salle pour {string} du {string} au {string}")
    public void reserveThisRoom(String personName, String start, String end) throws Exception {
        String body = buildReservationBody(lastCreatedRoomId, personName, start, end);
        lastResult = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
        if (lastResult.andReturn().getResponse().getStatus() == 201) {
            String response = lastResult.andReturn().getResponse().getContentAsString();
            lastCreatedReservationId = objectMapper.readTree(response).get("id").asLong();
        }
    }

    @When("je réserve la salle {long} pour {string} du {string} au {string}")
    public void reserveRoomById(Long roomId, String personName, String start, String end) throws Exception {
        String body = buildReservationBody(roomId, personName, start, end);
        lastResult = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("j'annule cette réservation")
    public void cancelReservation() throws Exception {
        lastResult = mockMvc.perform(delete("/api/reservations/" + lastCreatedReservationId + "/cancel"));
    }

    @Then("la réponse HTTP doit être {int}")
    public void statusShouldBe(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @And("la réservation a le statut {string}")
    public void reservationStatusShouldBe(String expectedStatus) throws Exception {
        lastResult.andExpect(jsonPath("$.status").value(expectedStatus));
    }

    @And("la réservation est au nom de {string}")
    public void reservationPersonNameShouldBe(String expectedName) throws Exception {
        lastResult.andExpect(jsonPath("$.personName").value(expectedName));
    }

    private String buildReservationBody(Long roomId, String personName, String start, String end) {
        return "{\"roomId\":" + roomId + ",\"personName\":\"" + personName + "\",\"startDateTime\":\"" + start + "\",\"endDateTime\":\"" + end + "\"}";
    }
}
