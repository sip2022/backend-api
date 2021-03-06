package com.sip.api.services.impl;


import com.sip.api.domains.activity.Activity;
import com.sip.api.domains.role.Role;
import com.sip.api.domains.user.User;
import com.sip.api.dtos.activity.ActivityCreationDto;
import com.sip.api.dtos.activity.ActivityDto;
import com.sip.api.exceptions.BadRequestException;
import com.sip.api.exceptions.NotFoundException;
import com.sip.api.repositories.ActivityRepository;
import com.sip.api.services.ActivityService;
import com.sip.api.services.RoleService;
import com.sip.api.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {
    private final ActivityRepository activityRepository;
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public Activity findById(String activityId) {
        return activityRepository.findById(activityId).orElseThrow(() -> new NotFoundException("Activity not found!"));
    }

    @Override
    public Activity createActivity(ActivityCreationDto activityCreationDto) {
        if (activityRepository.existsByName(activityCreationDto.getName()))
            throw new BadRequestException("Activity already exists!");

        checkNegativities(activityCreationDto.getBasePrice(), activityCreationDto.getAttendeesLimit());
        checkProfessorAssignedRoles(activityCreationDto.getProfessor().getId());

        return activityRepository.save(Activity.builder()
                .name(activityCreationDto.getName())
                .basePrice(activityCreationDto.getBasePrice())
                .professor(userService.findById(activityCreationDto.getProfessor().getId()))
                .attendeesLimit(activityCreationDto.getAttendeesLimit())
                .build());
    }

    @Override
    public Activity updateActivity(String activityId, ActivityDto activityDto) {
        Activity activity = findById(activityId);

        checkNegativities(activityDto.getBasePrice(), activityDto.getAttendeesLimit());
        checkProfessorAssignedRoles(activityDto.getProfessor().getId());

        activity.setName(activityDto.getName());
        activity.setBasePrice(activityDto.getBasePrice());
        activity.setAttendeesLimit(activityDto.getAttendeesLimit());
        activity.setProfessor(userService.findById(activityDto.getProfessor().getId()));
        return activityRepository.save(activity);
    }

    private void checkProfessorAssignedRoles(String userId) {
        if (!userHasAnyOfTheseRoles(userId, List.of(roleService.findByName("ROLE_ADMIN"),
                roleService.findByName("ROLE_PROFESSOR"))))
            throw new BadRequestException("User does not have ROLE_PROFESSOR assigned!");
    }

    private boolean userHasAnyOfTheseRoles(String userId, List<Role> roles) {
        final User user = userService.findById(userId);
        return user.getRoles().stream()
                .anyMatch(role -> roles.stream()
                        .anyMatch(r -> r.getName().equals(role.getName())));
    }

    private void checkNegativities(Double price, int attendeeLimit) {
        checkNegativePrice(price);
        checkNegativeAttendeeLimit(attendeeLimit);
    }

    private void checkNegativePrice(Double price) {
        if (price < 0) throw new BadRequestException("Activity price cannot be negative!");
    }

    private void checkNegativeAttendeeLimit(int attendeeLimit) {
        if (attendeeLimit < 0) throw new BadRequestException("Activity attendee limit cannot be negative!");
    }

    @Override
    public void delete(String activityId) {
        if (!activityRepository.existsById(activityId))
            throw new BadRequestException("Activity not found!");
        activityRepository.deleteById(activityId);
    }
}
