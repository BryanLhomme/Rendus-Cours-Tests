package com.example.exception;

public class ReservationAlreadyCancelledException extends RuntimeException {

    public ReservationAlreadyCancelledException(Long id) {
        super("La réservation " + id + " est déjà annulée");
    }
}
