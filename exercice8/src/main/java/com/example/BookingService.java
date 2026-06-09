package com.example;

import java.util.List;

public class BookingService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public BookingService(RoomRepository roomRepository,
                          ReservationRepository reservationRepository,
                          NotificationService notificationService) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    public BookingResult book(Reservation reservation) {
        if (!roomRepository.exists(reservation.getRoomCode())) {
            return new BookingResult(false, "Salle inconnue : " + reservation.getRoomCode());
        }

        if (!reservation.getEndTime().isAfter(reservation.getStartTime())) {
            return new BookingResult(false, "Période invalide");
        }

        Room room = roomRepository.findByCode(reservation.getRoomCode());

        if (reservation.getParticipantCount() > room.getMaxCapacity()) {
            return new BookingResult(false, "Capacité insuffisante");
        }

        List<Reservation> existing = reservationRepository.findByRoomCode(reservation.getRoomCode());
        for (Reservation ex : existing) {
            if (reservation.getStartTime().isBefore(ex.getEndTime()) &&
                reservation.getEndTime().isAfter(ex.getStartTime())) {
                return new BookingResult(false, "Salle déjà réservée sur ce créneau");
            }
        }

        notificationService.sendConfirmation(reservation.getUserEmail(), reservation.getRoomCode());
        return new BookingResult(true, "Réservation confirmée");
    }
}
