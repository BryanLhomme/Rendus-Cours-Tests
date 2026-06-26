package com.example.integration;

import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Tests d'intégration de l'API salles et réservations")
class ReservationApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    @DisplayName("Parcours complet : créer une salle, réserver, consulter et annuler")
    void shouldCreateRoomReserveAndCancel() throws Exception {
        // Créer une salle
        String roomResponse = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle Fusion\",\"capacity\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Salle Fusion"))
                .andReturn().getResponse().getContentAsString();
        Long roomId = objectMapper.readTree(roomResponse).get("id").asLong();

        // Créer une réservation
        String reservationBody = "{\"roomId\":" + roomId + ",\"personName\":\"Marie Martin\",\"startDateTime\":\"2024-06-15T10:00:00\",\"endDateTime\":\"2024-06-15T12:00:00\"}";
        String reservationResponse = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personName").value("Marie Martin"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andReturn().getResponse().getContentAsString();
        Long reservationId = objectMapper.readTree(reservationResponse).get("id").asLong();

        // Consulter la réservation
        mockMvc.perform(get("/api/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personName").value("Marie Martin"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        // Annuler la réservation
        mockMvc.perform(delete("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    @DisplayName("Réservation sur créneau déjà pris doit retourner 409")
    void shouldReturn409WhenSlotIsAlreadyTaken() throws Exception {
        // Créer une salle
        String roomResponse = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle Étoile\",\"capacity\":8}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long roomId = objectMapper.readTree(roomResponse).get("id").asLong();

        // Première réservation
        String body = "{\"roomId\":" + roomId + ",\"personName\":\"Jean Dupont\",\"startDateTime\":\"2024-06-15T10:00:00\",\"endDateTime\":\"2024-06-15T12:00:00\"}";
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        // Réservation en conflit
        String conflictBody = "{\"roomId\":" + roomId + ",\"personName\":\"Marie Martin\",\"startDateTime\":\"2024-06-15T11:00:00\",\"endDateTime\":\"2024-06-15T13:00:00\"}";
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conflictBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Annuler deux fois une réservation doit retourner 409")
    void shouldReturn409WhenCancellingAlreadyCancelledReservation() throws Exception {
        // Créer une salle
        String roomResponse = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle Atlas\",\"capacity\":5}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long roomId = objectMapper.readTree(roomResponse).get("id").asLong();

        // Créer une réservation
        String reservationBody = "{\"roomId\":" + roomId + ",\"personName\":\"Jean Dupont\",\"startDateTime\":\"2024-06-15T14:00:00\",\"endDateTime\":\"2024-06-15T16:00:00\"}";
        String reservationResponse = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationBody))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long reservationId = objectMapper.readTree(reservationResponse).get("id").asLong();

        // Première annulation
        mockMvc.perform(delete("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isOk());

        // Deuxième annulation → 409
        mockMvc.perform(delete("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }
}
