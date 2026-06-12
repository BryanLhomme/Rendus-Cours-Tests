package com.example.repository;

import com.example.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    List<Reservation> findByRoomId(Long roomId);

    void deleteAll();
}
