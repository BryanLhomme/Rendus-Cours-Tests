package com.example.service;

import com.example.exception.InvalidStatusTransitionException;
import com.example.exception.TicketNotFoundException;
import com.example.model.Priority;
import com.example.model.Ticket;
import com.example.model.TicketStatus;
import com.example.repository.TicketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du service TicketService")
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("La création d'un ticket doit retourner un ticket avec le statut OPEN")
    void shouldCreateTicketWithOpenStatus() {
        // Arrange
        String title = "Bug de connexion";
        Priority priority = Priority.HIGH;
        Ticket savedTicket = new Ticket(title, priority);
        savedTicket.setId(1L);
        when(repository.save(any(Ticket.class))).thenReturn(savedTicket);

        // Act
        Ticket result = ticketService.create(title, priority);

        // Assert
        assertEquals(TicketStatus.OPEN, result.getStatus());
        assertEquals(title, result.getTitle());
        verify(repository).save(any(Ticket.class));
    }

    @Test
    @DisplayName("La recherche d'un ticket existant doit retourner le ticket")
    void shouldReturnTicketWhenFound() {
        // Arrange
        Long ticketId = 1L;
        Ticket ticket = new Ticket("Bug de connexion", Priority.HIGH);
        ticket.setId(ticketId);
        when(repository.findById(ticketId)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = ticketService.getById(ticketId);

        // Assert
        assertEquals(ticketId, result.getId());
        verify(repository).findById(ticketId);
    }

    @Test
    @DisplayName("La recherche d'un ticket inexistant doit lancer une exception")
    void shouldThrowExceptionWhenTicketNotFound() {
        // Arrange
        Long ticketId = 99L;
        when(repository.findById(ticketId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TicketNotFoundException.class, () -> ticketService.getById(ticketId));
        verify(repository).findById(ticketId);
    }

    @Test
    @DisplayName("La transition OPEN vers IN_PROGRESS doit être autorisée")
    void shouldAllowTransitionFromOpenToInProgress() {
        // Arrange
        Ticket ticket = new Ticket("Problème réseau", Priority.MEDIUM);
        ticket.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = ticketService.updateStatus(1L, TicketStatus.IN_PROGRESS);

        // Assert
        assertEquals(TicketStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    @DisplayName("La transition OPEN vers RESOLVED doit être autorisée")
    void shouldAllowTransitionFromOpenToResolved() {
        // Arrange
        Ticket ticket = new Ticket("Erreur serveur", Priority.HIGH);
        ticket.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = ticketService.updateStatus(1L, TicketStatus.RESOLVED);

        // Assert
        assertEquals(TicketStatus.RESOLVED, result.getStatus());
    }

    @Test
    @DisplayName("La transition IN_PROGRESS vers RESOLVED doit être autorisée")
    void shouldAllowTransitionFromInProgressToResolved() {
        // Arrange
        Ticket ticket = new Ticket("Lenteur application", Priority.LOW);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = ticketService.updateStatus(1L, TicketStatus.RESOLVED);

        // Assert
        assertEquals(TicketStatus.RESOLVED, result.getStatus());
    }

    @Test
    @DisplayName("La modification d'un ticket RESOLVED doit être refusée")
    void shouldRejectTransitionFromResolvedStatus() {
        // Arrange
        Ticket ticket = new Ticket("Bug de connexion", Priority.HIGH);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.RESOLVED);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThrows(InvalidStatusTransitionException.class,
                () -> ticketService.updateStatus(1L, TicketStatus.IN_PROGRESS));
        verify(repository, never()).save(any(Ticket.class));
    }

    @Test
    @DisplayName("La transition IN_PROGRESS vers OPEN doit être refusée")
    void shouldRejectTransitionFromInProgressToOpen() {
        // Arrange
        Ticket ticket = new Ticket("Crash au démarrage", Priority.HIGH);
        ticket.setId(1L);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThrows(InvalidStatusTransitionException.class,
                () -> ticketService.updateStatus(1L, TicketStatus.OPEN));
    }
}
