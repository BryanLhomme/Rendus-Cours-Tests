package com.example.repository;

import com.example.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(idGenerator.getAndIncrement());
        }
        store.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Reservation> findByRoomId(Long roomId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation : store.values()) {
            if (reservation.getRoomId().equals(roomId)) {
                result.add(reservation);
            }
        }
        return result;
    }

    @Override
    public void deleteAll() {
        store.clear();
        idGenerator.set(1);
    }
}
