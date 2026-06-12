package com.example.model;

import java.time.LocalDateTime;

public class Reservation {

    private Long id;
    private Long roomId;
    private String personName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private ReservationStatus status;

    public Reservation(Long roomId, String personName, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.roomId = roomId;
        this.personName = personName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = ReservationStatus.CONFIRMED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getPersonName() {
        return personName;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
