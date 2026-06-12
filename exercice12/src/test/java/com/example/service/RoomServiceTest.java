package com.example.service;

import com.example.model.Room;
import com.example.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du service RoomService")
class RoomServiceTest {

    @Mock
    private RoomRepository repository;

    @InjectMocks
    private RoomService roomService;

    @Test
    @DisplayName("La création d'une salle doit retourner la salle avec son nom et sa capacité")
    void shouldCreateRoomAndReturnIt() {
        // Arrange
        Room savedRoom = new Room("Salle Fusion", 10);
        savedRoom.setId(1L);
        when(repository.save(any(Room.class))).thenReturn(savedRoom);

        // Act
        Room result = roomService.create("Salle Fusion", 10);

        // Assert
        assertEquals("Salle Fusion", result.getName());
        assertEquals(10, result.getCapacity());
        verify(repository).save(any(Room.class));
    }

    @Test
    @DisplayName("La liste des salles doit retourner toutes les salles enregistrées")
    void shouldReturnAllRooms() {
        // Arrange
        List<Room> rooms = new ArrayList<>();
        Room room = new Room("Salle Fusion", 10);
        room.setId(1L);
        rooms.add(room);
        when(repository.findAll()).thenReturn(rooms);

        // Act
        List<Room> result = roomService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Salle Fusion", result.get(0).getName());
        verify(repository).findAll();
    }
}
