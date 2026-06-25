package com.example.controller;

import com.example.exception.RoomNotFoundException;
import com.example.model.Room;
import com.example.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
@DisplayName("Tests du contrôleur RoomController")
class RoomControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    @Test
    @DisplayName("POST /api/rooms doit retourner 201 avec la salle créée")
    void shouldReturn201WhenRoomIsCreated() throws Exception {
        // Arrange
        Room room = new Room("Salle Fusion", 10);
        room.setId(1L);
        when(roomService.create(eq("Salle Fusion"), eq(10))).thenReturn(room);

        // Act + Assert
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle Fusion\",\"capacity\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Salle Fusion"))
                .andExpect(jsonPath("$.capacity").value(10));

        verify(roomService).create("Salle Fusion", 10);
    }

    @Test
    @DisplayName("POST /api/rooms avec un nom vide doit retourner 400")
    void shouldReturn400WhenNameIsBlank() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"capacity\":10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(roomService, never()).create(anyString(), anyInt());
    }

    @Test
    @DisplayName("POST /api/rooms avec une capacité de 0 doit retourner 400")
    void shouldReturn400WhenCapacityIsZero() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle Fusion\",\"capacity\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verify(roomService, never()).create(anyString(), anyInt());
    }

    @Test
    @DisplayName("GET /api/rooms doit retourner 200 avec la liste des salles")
    void shouldReturn200WithRoomList() throws Exception {
        // Arrange
        Room room1 = new Room("Salle Fusion", 10);
        room1.setId(1L);
        Room room2 = new Room("Salle Étoile", 20);
        room2.setId(2L);
        when(roomService.findAll()).thenReturn(List.of(room1, room2));

        // Act + Assert
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Salle Fusion"))
                .andExpect(jsonPath("$[1].name").value("Salle Étoile"));

        verify(roomService).findAll();
    }

    @Test
    @DisplayName("GET /api/rooms doit retourner 200 avec une liste vide")
    void shouldReturn200WithEmptyList() throws Exception {
        // Arrange
        when(roomService.findAll()).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(roomService).findAll();
    }
}
