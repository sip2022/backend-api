package com.sip.api.domains.timeslot;

import com.sip.api.dtos.timeslot.TimeslotCreationDto;
import com.sip.api.dtos.timeslot.TimeslotDto;

import java.util.List;
import java.util.stream.Collectors;

public class TimeslotConverter {

    public static List<TimeslotDto> fromEntityToDto(List<Timeslot> timeslots) {
        return timeslots.stream()
                .map(TimeslotConverter::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public static TimeslotDto fromEntityToDto(Timeslot timeslot) {
        return TimeslotDto.builder()
                .id(timeslot.getId())
                .startTime(timeslot.getStartTime())
                .endTime(timeslot.getEndTime())
                .build();
    }

    public static Timeslot fromDtoToEntity(TimeslotDto timeslotDto) {
        return Timeslot.builder()
                .startTime(timeslotDto.getStartTime())
                .endTime(timeslotDto.getEndTime())
                .build();
    }

    public static Timeslot fromDtoToEntity(TimeslotCreationDto timeslotCreationDto){
        return Timeslot.builder()
                .startTime(timeslotCreationDto.getStartTime())
                .endTime(timeslotCreationDto.getEndTime())
                .build();
    }
}
