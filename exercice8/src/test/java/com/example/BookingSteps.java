package com.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingSteps {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;
    private NotificationService notificationService;
    private BookingService bookingService;
    private BookingResult result;

    @Given("une salle avec le code {string} de nom {string} et une capacité de {int} personnes")
    public void uneSalleAvecLeCode(String code, String name, int capacity) {
        roomRepository = mock(RoomRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        notificationService = mock(NotificationService.class);
        bookingService = new BookingService(roomRepository, reservationRepository, notificationService);

        Room room = new Room(code, name, capacity);
        when(roomRepository.exists(code)).thenReturn(true);
        when(roomRepository.findByCode(code)).thenReturn(room);
    }

    @Given("aucune salle avec le code {string}")
    public void aucuneSalleAvecLeCode(String code) {
        roomRepository = mock(RoomRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        notificationService = mock(NotificationService.class);
        bookingService = new BookingService(roomRepository, reservationRepository, notificationService);

        when(roomRepository.exists(code)).thenReturn(false);
    }

    @And("aucune réservation existante pour la salle {string}")
    public void aucuneReservationExistante(String roomCode) {
        when(reservationRepository.findByRoomCode(roomCode)).thenReturn(Collections.emptyList());
    }

    @And("une réservation existante pour la salle {string} du {string} au {string}")
    public void uneReservationExistante(String roomCode, String start, String end) {
        Reservation existing = new Reservation(
                "existing@example.com", roomCode, 1,
                LocalDateTime.parse(start, FORMATTER),
                LocalDateTime.parse(end, FORMATTER)
        );
        when(reservationRepository.findByRoomCode(roomCode)).thenReturn(List.of(existing));
    }

    @When("je réserve la salle {string} pour {string} avec {int} participants du {string} au {string}")
    public void jeReserveLaSalle(String roomCode, String email, int participants, String start, String end) {
        Reservation reservation = new Reservation(
                email, roomCode, participants,
                LocalDateTime.parse(start, FORMATTER),
                LocalDateTime.parse(end, FORMATTER)
        );
        result = bookingService.book(reservation);
    }

    @When("je tente de réserver la salle {string} pour {string} avec {int} participants du {string} au {string}")
    public void jeTenteDeReserverLaSalle(String roomCode, String email, int participants, String start, String end) {
        Reservation reservation = new Reservation(
                email, roomCode, participants,
                LocalDateTime.parse(start, FORMATTER),
                LocalDateTime.parse(end, FORMATTER)
        );
        result = bookingService.book(reservation);
    }

    @Then("la réservation est acceptée")
    public void laReservationEstAcceptee() {
        assertTrue(result.isAccepted());
    }

    @And("le message de confirmation est {string}")
    public void leMessageDeConfirmationEst(String expected) {
        assertEquals(expected, result.getMessage());
    }

    @Then("la réservation est refusée avec le message {string}")
    public void laReservationEstRefuseeAvecLeMessage(String expected) {
        assertFalse(result.isAccepted());
        assertEquals(expected, result.getMessage());
    }

    @Then("une notification de confirmation est envoyée à {string} pour la salle {string}")
    public void uneNotificationEstEnvoyee(String email, String roomCode) {
        verify(notificationService).sendConfirmation(email, roomCode);
    }

    @Then("aucune notification n'est envoyée")
    public void aucuneNotificationEstEnvoyee() {
        verifyNoInteractions(notificationService);
    }
}
