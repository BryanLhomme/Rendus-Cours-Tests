package com.example.dto;

import com.example.model.Ticket;

public class TicketResponse {

    private Long id;
    private String title;
    private String priority;
    private String status;

    public TicketResponse(Long id, String title, String priority, String status) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.status = status;
    }

    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getPriority().name(),
                ticket.getStatus().name()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }
}
