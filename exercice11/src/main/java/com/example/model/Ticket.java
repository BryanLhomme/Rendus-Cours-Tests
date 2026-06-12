package com.example.model;

public class Ticket {

    private Long id;
    private String title;
    private Priority priority;
    private TicketStatus status;

    public Ticket(String title, Priority priority) {
        this.title = title;
        this.priority = priority;
        this.status = TicketStatus.OPEN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Priority getPriority() {
        return priority;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}
