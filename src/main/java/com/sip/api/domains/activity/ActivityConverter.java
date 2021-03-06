package com.sip.api.domains.activity;

import com.sip.api.domains.user.UserConverter;
import com.sip.api.dtos.activity.ActivityDto;

import java.util.List;

public class ActivityConverter {
    public static List<ActivityDto> fromEntityToDto(List<Activity> activities) {
        return activities.stream()
                .map(ActivityConverter::fromEntityToDto)
                .collect(java.util.stream.Collectors.toList());
    }

    public static ActivityDto fromEntityToDto(Activity activity) {
        return ActivityDto.builder()
                .id(activity.getId())
                .name(activity.getName())
                .basePrice(activity.getBasePrice())
                .professor(UserConverter.entityToDtoSlim(activity.getProfessor()))
                .attendeesLimit(activity.getAttendeesLimit())
                .build();
    }

    public static Activity fromDtoToEntity(ActivityDto activityDto) {
        return Activity.builder()
                .name(activityDto.getName())
                .basePrice(activityDto.getBasePrice())
                .professor(UserConverter.slimDtoToEntity(activityDto.getProfessor()))
                .attendeesLimit(activityDto.getAttendeesLimit())
                .build();
    }
}
