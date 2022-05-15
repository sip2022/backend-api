package com.sip.api.controllers;

import com.sip.api.domains.reservation.ReservationConverter;
import com.sip.api.dtos.reservation.ReservationCreationDto;
import com.sip.api.dtos.reservation.ReservationDto;
import com.sip.api.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping("/all")
    public List<ReservationDto> findAll() {
        return ReservationConverter.fromEntityToDto(reservationService.findAll());
    }

    @GetMapping("/{reservationId}")
    public ReservationDto findById(@PathVariable("reservationId") String reservationId) {
        return ReservationConverter.fromEntityToDto(reservationService.findById(reservationId));
    }

    @PostMapping()
    public ReservationDto addUserToReservation(@RequestBody ReservationCreationDto reservationCreationDto) {
        return ReservationConverter.fromEntityToDto(reservationService.addUserToReservation(reservationCreationDto));
    }

    @PutMapping("/{reservationId}/{attendeeId}")
    public ReservationDto removeUserFromReservation(@PathVariable("reservationId") String reservationId, @PathVariable("attendeeId") String attendeeId) {
        return ReservationConverter.fromEntityToDto(reservationService.removeUserFromReservation(reservationId, attendeeId));
    }

    @DeleteMapping("/{reservationId}")
    public void deleteReservation(@PathVariable("reservationId") String reservationId) {
        reservationService.deleteReservation(reservationId);
    }
}
