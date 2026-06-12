package com.example.exception;

public class ReservationConflictException extends RuntimeException {

    public ReservationConflictException() {
        super("Le créneau est déjà réservé pour cette salle");
    }
}
