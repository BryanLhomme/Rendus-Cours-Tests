package com.example.service;

import com.example.exception.ReservationAlreadyCancelledException;
import com.example.exception.ReservationConflictException;
import com.example.exception.ReservationNotFoundException;
import com.example.exception.RoomNotFoundException;
import com.example.model.Reservation;
import com.example.model.ReservationStatus;
import com.example.model.Room;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du service ReservationService")
class ReservationServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private static final LocalDateTime START = LocalDateTime.of(2024, 6, 15, 10, 0);
    private static final LocalDateTime END = LocalDateTime.of(2024, 6, 15, 12, 0);

    @Test
    @DisplayName("La création d'une réservation valide doit retourner une réservation avec le statut CONFIRMED")
    void shouldCreateReservationWithConfirmedStatus() {
        // Arrange
        Long roomId = 1L;
        Room room = new Room("Salle Fusion", 10);
        room.setId(roomId);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(reservationRepository.findByRoomId(roomId)).thenReturn(new ArrayList<>());
        Reservation saved = new Reservation(roomId, "Marie Martin", START, END);
        saved.setId(1L);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(saved);

        // Act
        Reservation result = reservationService.create(roomId, "Marie Martin", START, END);

        // Assert
        assertEquals(ReservationStatus.CONFIRMED, result.getStatus());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("La création d'une réservation doit échouer si la salle n'existe pas")
    void shouldThrowExceptionWhenRoomDoesNotExist() {
        // Arrange
        Long roomId = 99L;
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoomNotFoundException.class,
                () -> reservationService.create(roomId, "Marie Martin", START, END));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("La création d'une réservation doit échouer si la date de fin est avant la date de début")
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        // Arrange
        Long roomId = 1L;
        Room room = new Room("Salle Fusion", 10);
        room.setId(roomId);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        LocalDateTime invalidEnd = START.minusHours(1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> reservationService.create(roomId, "Marie Martin", START, invalidEnd));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("La création d'une réservation doit échouer si le créneau chevauche une réservation existante")
    void shouldThrowExceptionWhenSlotOverlapsExistingReservation() {
        // Arrange
        Long roomId = 1L;
        Room room = new Room("Salle Fusion", 10);
        room.setId(roomId);
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        Reservation existing = new Reservation(roomId, "Jean Dupont", START, END);
        existing.setId(1L);
        when(reservationRepository.findByRoomId(roomId)).thenReturn(List.of(existing));

        LocalDateTime overlappingStart = START.plusMinutes(30);
        LocalDateTime overlappingEnd = END.plusHours(1);

        // Act & Assert
        assertThrows(ReservationConflictException.class,
                () -> reservationService.create(roomId, "Marie Martin", overlappingStart, overlappingEnd));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("La recherche d'une réservation existante doit retourner la réservation")
    void shouldReturnReservationWhenFound() {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation(1L, "Marie Martin", START, END);
        reservation.setId(reservationId);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        Reservation result = reservationService.getById(reservationId);

        // Assert
        assertEquals(reservationId, result.getId());
        verify(reservationRepository).findById(reservationId);
    }

    @Test
    @DisplayName("La recherche d'une réservation inexistante doit lancer une exception")
    void shouldThrowExceptionWhenReservationNotFound() {
        // Arrange
        Long reservationId = 99L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReservationNotFoundException.class,
                () -> reservationService.getById(reservationId));
        verify(reservationRepository).findById(reservationId);
    }

    @Test
    @DisplayName("L'annulation d'une réservation confirmée doit changer le statut en CANCELLED")
    void shouldCancelConfirmedReservation() {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation(1L, "Marie Martin", START, END);
        reservation.setId(reservationId);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        Reservation result = reservationService.cancel(reservationId);

        // Assert
        assertEquals(ReservationStatus.CANCELLED, result.getStatus());
    }

    @Test
    @DisplayName("L'annulation d'une réservation déjà annulée doit être refusée")
    void shouldThrowExceptionWhenReservationAlreadyCancelled() {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation(1L, "Marie Martin", START, END);
        reservation.setId(reservationId);
        reservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThrows(ReservationAlreadyCancelledException.class,
                () -> reservationService.cancel(reservationId));
    }
}
