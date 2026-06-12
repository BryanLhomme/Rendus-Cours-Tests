package com.example.exception;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(Long id) {
        super("Salle " + id + " introuvable");
    }
}
