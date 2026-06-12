package com.example.controller;

import com.example.exception.InvalidStatusTransitionException;
import com.example.exception.TicketNotFoundException;
import com.example.model.Priority;
import com.example.model.Ticket;
import com.example.model.TicketStatus;
import com.example.service.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@DisplayName("Tests du contrôleur TicketController")
class TicketControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @Test
    @DisplayName("POST /api/tickets doit retourner 201 avec le ticket créé")
    void shouldReturn201WhenTicketIsCreated() throws Exception {
        // Arrange
        Ticket ticket = new Ticket("Bug de connexion", Priority.HIGH);
        ticket.setId(1L);
        when(ticketService.create(eq("Bug de connexion"), eq(Priority.HIGH))).thenReturn(ticket);

        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Bug de connexion\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Bug de connexion"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(ticketService).create("Bug de connexion", Priority.HIGH);
    }

    @Test
    @DisplayName("POST /api/tickets avec un titre trop court doit retourner 400")
    void shouldReturn400WhenTitleIsTooShort() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"ab\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(ticketService, never()).create(any(), any());
    }

    @Test
    @DisplayName("POST /api/tickets sans priorité doit retourner 400")
    void shouldReturn400WhenPriorityIsMissing() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Bug de connexion\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(ticketService, never()).create(any(), any());
    }

    @Test
    @DisplayName("GET /api/tickets/{id} doit retourner 200 avec le ticket")
    void shouldReturn200WithTicketWhenFound() throws Exception {
        // Arrange
        Long ticketId = 1L;
        Ticket ticket = new Ticket("Problème réseau", Priority.MEDIUM);
        ticket.setId(ticketId);
        when(ticketService.getById(ticketId)).thenReturn(ticket);

        // Act + Assert
        mockMvc.perform(get("/api/tickets/" + ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId))
                .andExpect(jsonPath("$.title").value("Problème réseau"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(ticketService).getById(ticketId);
    }

    @Test
    @DisplayName("GET /api/tickets/{id} doit retourner 404 si le ticket n'existe pas")
    void shouldReturn404WhenTicketNotFound() throws Exception {
        // Arrange
        Long ticketId = 99L;
        when(ticketService.getById(ticketId)).thenThrow(new TicketNotFoundException(ticketId));

        // Act + Assert
        mockMvc.perform(get("/api/tickets/" + ticketId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());

        verify(ticketService).getById(ticketId);
    }

    @Test
    @DisplayName("PATCH /api/tickets/{id}/status doit retourner 200 avec le nouveau statut")
    void shouldReturn200WhenStatusIsUpdated() throws Exception {
        // Arrange
        Long ticketId = 1L;
        Ticket ticket = new Ticket("Bug de connexion", Priority.HIGH);
        ticket.setId(ticketId);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        when(ticketService.updateStatus(eq(ticketId), eq(TicketStatus.IN_PROGRESS))).thenReturn(ticket);

        // Act + Assert
        mockMvc.perform(patch("/api/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newStatus\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(ticketService).updateStatus(ticketId, TicketStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("PATCH /api/tickets/{id}/status doit retourner 409 si la transition est invalide")
    void shouldReturn409WhenStatusTransitionIsInvalid() throws Exception {
        // Arrange
        Long ticketId = 1L;
        when(ticketService.updateStatus(eq(ticketId), any(TicketStatus.class)))
                .thenThrow(new InvalidStatusTransitionException(TicketStatus.RESOLVED, TicketStatus.IN_PROGRESS));

        // Act + Assert
        mockMvc.perform(patch("/api/tickets/" + ticketId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newStatus\":\"IN_PROGRESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());

        verify(ticketService).updateStatus(ticketId, TicketStatus.IN_PROGRESS);
    }
}
