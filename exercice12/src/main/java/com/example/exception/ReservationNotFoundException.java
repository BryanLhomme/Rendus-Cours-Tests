package com.example.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(Long id) {
        super("Réservation " + id + " introuvable");
    }
}
