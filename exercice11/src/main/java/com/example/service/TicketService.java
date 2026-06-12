package com.example.service;

import com.example.exception.InvalidStatusTransitionException;
import com.example.exception.TicketNotFoundException;
import com.example.model.Priority;
import com.example.model.Ticket;
import com.example.model.TicketStatus;
import com.example.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket create(String title, Priority priority) {
        Ticket ticket = new Ticket(title, priority);
        return repository.save(ticket);
    }

    public Ticket getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    public List<Ticket> findAll() {
        return repository.findAll();
    }

    public Ticket updateStatus(Long id, TicketStatus newStatus) {
        Ticket ticket = getById(id);

        if (!isValidTransition(ticket.getStatus(), newStatus)) {
            throw new InvalidStatusTransitionException(ticket.getStatus(), newStatus);
        }

        ticket.setStatus(newStatus);
        return ticket;
    }

    private boolean isValidTransition(TicketStatus current, TicketStatus next) {
        return switch (current) {
            case OPEN -> next == TicketStatus.IN_PROGRESS || next == TicketStatus.RESOLVED;
            case IN_PROGRESS -> next == TicketStatus.RESOLVED;
            case RESOLVED -> false;
        };
    }
}
