package com.example.repository;

import com.example.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {

    Ticket save(Ticket ticket);

    Optional<Ticket> findById(Long id);

    List<Ticket> findAll();

    void deleteAll();
}
