package com.sip.api.domains.reservation;

import com.sip.api.domains.availableClass.AvailableClassConverter;
import com.sip.api.dtos.availableClass.AvailableClassAttendeeAmountDto;
import com.sip.api.dtos.reservation.RemainingReservationsDto;
import com.sip.api.dtos.reservation.ReservationDto;
import com.sip.api.dtos.user.UserSlimDto;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationConverter {
    public static List<ReservationDto> fromEntityToDto(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationConverter::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public static ReservationDto fromEntityToDto(Reservation reservation) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .availableClass(AvailableClassConverter.fromEntityToDto(reservation.getAvailableClass()))
                .attendees(reservation.getAttendees().stream()
                        .map(attendee -> UserSlimDto.builder()
                                .id(attendee.getId())
                                .email(attendee.getEmail())
                                .firstName(attendee.getFirstName())
                                .lastName(attendee.getLastName())
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    public static AvailableClassAttendeeAmountDto toAvailableClassDto(Integer attendeeAmount) {
        return AvailableClassAttendeeAmountDto.builder()
                .attendeeAmount(attendeeAmount)
                .build();
    }


    public static RemainingReservationsDto toRemaminingReservationsDto(Integer remainingReservations) {
        return RemainingReservationsDto.builder()
                .remainingReservations(remainingReservations)
                .build();
    }
}
