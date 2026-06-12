package com.example.repository;

import com.example.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTicketRepository implements TicketRepository {

    private final Map<Long, Ticket> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Ticket save(Ticket ticket) {
        if (ticket.getId() == null) {
            ticket.setId(idGenerator.getAndIncrement());
        }
        store.put(ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Ticket> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteAll() {
        store.clear();
        idGenerator.set(1);
    }
}
