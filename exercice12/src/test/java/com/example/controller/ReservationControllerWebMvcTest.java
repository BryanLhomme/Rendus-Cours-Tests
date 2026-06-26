package com.example.controller;

import com.example.exception.ReservationAlreadyCancelledException;
import com.example.exception.ReservationConflictException;
import com.example.exception.ReservationNotFoundException;
import com.example.exception.RoomNotFoundException;
import com.example.model.Reservation;
import com.example.model.ReservationStatus;
import com.example.service.ReservationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@DisplayName("Tests du contrôleur ReservationController")
class ReservationControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    private static final LocalDateTime START = LocalDateTime.of(2024, 6, 15, 10, 0);
    private static final LocalDateTime END = LocalDateTime.of(2024, 6, 15, 12, 0);
    private static final String BODY = "{\"roomId\":1,\"personName\":\"Marie Martin\",\"startDateTime\":\"2024-06-15T10:00:00\",\"endDateTime\":\"2024-06-15T12:00:00\"}";

    @Test
    @DisplayName("POST /api/reservations doit retourner 201 avec la réservation créée")
    void shouldReturn201WhenReservationIsCreated() throws Exception {
        // Arrange
        Reservation reservation = new Reservation(1L, "Marie Martin", START, END);
        reservation.setId(1L);
        when(reservationService.create(eq(1L), eq("Marie Martin"), any(), any())).thenReturn(reservation);

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.personName").value("Marie Martin"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(reservationService).create(eq(1L), eq("Marie Martin"), any(), any());
    }

    @Test
    @DisplayName("POST /api/reservations avec un nom vide doit retourner 400")
    void shouldReturn400WhenPersonNameIsBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":1,\"personName\":\"\",\"startDateTime\":\"2024-06-15T10:00:00\",\"endDateTime\":\"2024-06-15T12:00:00\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(reservationService, never()).create(any(), anyString(), any(), any());
    }

    @Test
    @DisplayName("POST /api/reservations sans roomId doit retourner 400")
    void shouldReturn400WhenRoomIdIsMissing() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"personName\":\"Marie Martin\",\"startDateTime\":\"2024-06-15T10:00:00\",\"endDateTime\":\"2024-06-15T12:00:00\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(reservationService, never()).create(any(), anyString(), any(), any());
    }

    @Test
    @DisplayName("POST /api/reservations doit retourner 404 si la salle est introuvable")
    void shouldReturn404WhenRoomDoesNotExist() throws Exception {
        // Arrange
        when(reservationService.create(any(), anyString(), any(), any()))
                .thenThrow(new RoomNotFoundException(1L));

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /api/reservations doit retourner 409 si le créneau est déjà pris")
    void shouldReturn409WhenSlotConflict() throws Exception {
        // Arrange
        when(reservationService.create(any(), anyString(), any(), any()))
                .thenThrow(new ReservationConflictException());

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /api/reservations doit retourner 400 si les dates sont invalides")
    void shouldReturn400WhenDatesAreInvalid() throws Exception {
        // Arrange
        when(reservationService.create(any(), anyString(), any(), any()))
                .thenThrow(new IllegalArgumentException("La date de fin doit être après la date de début"));

        // Act + Assert
        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":1,\"personName\":\"Marie Martin\",\"startDateTime\":\"2024-06-15T12:00:00\",\"endDateTime\":\"2024-06-15T10:00:00\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("GET /api/reservations/{id} doit retourner 200 avec la réservation")
    void shouldReturn200WhenReservationFound() throws Exception {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation(1L, "Marie Martin", START, END);
        reservation.setId(reservationId);
        when(reservationService.getById(reservationId)).thenReturn(reservation);

        // Act + Assert
        mockMvc.perform(get("/api/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationId))
                .andExpect(jsonPath("$.personName").value("Marie Martin"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(reservationService).getById(reservationId);
    }

    @Test
    @DisplayName("GET /api/reservations/{id} doit retourner 404 si introuvable")
    void shouldReturn404WhenReservationNotFound() throws Exception {
        // Arrange
        Long reservationId = 99L;
        when(reservationService.getById(reservationId)).thenThrow(new ReservationNotFoundException(reservationId));

        // Act + Assert
        mockMvc.perform(get("/api/reservations/" + reservationId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());

        verify(reservationService).getById(reservationId);
    }

    @Test
    @DisplayName("DELETE /api/reservations/{id}/cancel doit retourner 200 avec le statut CANCELLED")
    void shouldReturn200WhenReservationCancelled() throws Exception {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation(1L, "Marie Martin", START, END);
        reservation.setId(reservationId);
        reservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationService.cancel(reservationId)).thenReturn(reservation);

        // Act + Assert
        mockMvc.perform(delete("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(reservationService).cancel(reservationId);
    }

    @Test
    @DisplayName("DELETE /api/reservations/{id}/cancel doit retourner 409 si déjà annulée")
    void shouldReturn409WhenAlreadyCancelled() throws Exception {
        // Arrange
        Long reservationId = 1L;
        when(reservationService.cancel(reservationId)).thenThrow(new ReservationAlreadyCancelledException(reservationId));

        // Act + Assert
        mockMvc.perform(delete("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());

        verify(reservationService).cancel(reservationId);
    }
}
