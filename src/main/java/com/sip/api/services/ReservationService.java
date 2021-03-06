package com.sip.api.services;

import com.sip.api.domains.reservation.Reservation;
import com.sip.api.dtos.reservation.ReservationCreationDto;

import java.util.List;

public interface ReservationService {
    List<Reservation> findAll();

    Reservation findById(String reservationId);

    List<Reservation> findAllByUserId(String userId);

    Integer countAttendeeAmountByAvailableClassId(String availableClassId);

    Integer countRemainingReservationsByUserId(String userId);

    Reservation addUserToReservation(ReservationCreationDto reservationCreationDto);

    Reservation removeUserFromReservationUsingAvailableClassId(String attendeeId, String availableClassId);

    void deleteReservation(String reservationId);
}
