package com.example.exception;

import com.example.model.TicketStatus;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(TicketStatus current, TicketStatus next) {
        super("Transition invalide : " + current + " vers " + next);
    }
}
