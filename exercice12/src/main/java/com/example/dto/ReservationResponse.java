package com.example.dto;

import com.example.model.Reservation;

public class ReservationResponse {

    private Long id;
    private Long roomId;
    private String personName;
    private String startDateTime;
    private String endDateTime;
    private String status;

    public ReservationResponse(Long id, Long roomId, String personName,
                               String startDateTime, String endDateTime, String status) {
        this.id = id;
        this.roomId = roomId;
        this.personName = personName;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRoomId(),
                reservation.getPersonName(),
                reservation.getStartDateTime().toString(),
                reservation.getEndDateTime().toString(),
                reservation.getStatus().name()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getPersonName() {
        return personName;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public String getStatus() {
        return status;
    }
}
