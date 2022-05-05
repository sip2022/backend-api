package com.sip.api.dtos.activity;

import com.sip.api.domains.appointment.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.NonNegative;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class ActivityDto {
    private String id;
    private String name;
    @NonNegative
    private Double basePrice;
    private Set<Appointment> appointments;
}
