package com.example.service;

import com.example.exception.ReservationAlreadyCancelledException;
import com.example.exception.ReservationConflictException;
import com.example.exception.ReservationNotFoundException;
import com.example.exception.RoomNotFoundException;
import com.example.model.Reservation;
import com.example.model.ReservationStatus;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    public Reservation create(Long roomId, String personName, LocalDateTime start, LocalDateTime end) {
        roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début");
        }

        List<Reservation> existing = reservationRepository.findByRoomId(roomId);
        for (Reservation r : existing) {
            if (r.getStatus() == ReservationStatus.CONFIRMED && overlaps(start, end, r.getStartDateTime(), r.getEndDateTime())) {
                throw new ReservationConflictException();
            }
        }

        Reservation reservation = new Reservation(roomId, personName, start, end);
        return reservationRepository.save(reservation);
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public Reservation cancel(Long id) {
        Reservation reservation = getById(id);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ReservationAlreadyCancelledException(id);
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservation;
    }

    private boolean overlaps(LocalDateTime startA, LocalDateTime endA, LocalDateTime startB, LocalDateTime endB) {
        return startA.isBefore(endB) && endA.isAfter(startB);
    }
}
