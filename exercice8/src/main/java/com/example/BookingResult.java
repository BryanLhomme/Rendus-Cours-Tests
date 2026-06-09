package com.example;

public class BookingResult {
    private final boolean accepted;
    private final String message;

    public BookingResult(boolean accepted, String message) {
        this.accepted = accepted;
        this.message = message;
    }

    public boolean isAccepted() { return accepted; }
    public String getMessage() { return message; }
}
