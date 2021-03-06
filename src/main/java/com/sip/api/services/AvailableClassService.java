package com.sip.api.services;

import com.sip.api.domains.availableClass.AvailableClass;
import com.sip.api.dtos.availableClass.AvailableClassDto;
import com.sip.api.dtos.availableClass.AvailableClassCreationDto;

import java.util.List;

public interface AvailableClassService {
    List<AvailableClass> findAll();

    AvailableClass findById(String appointmentId);

    List<AvailableClass> findByActivityId(String activityId);

    AvailableClass createAvailableClass(AvailableClassCreationDto availableClassCreationDto);

    void removeAvailableClass(String availableClassId);

    AvailableClass updateAvailableClass(AvailableClassDto availableClassDto);
}
