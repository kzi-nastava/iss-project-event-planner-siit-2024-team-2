package com.example.eventplanner.dto.event.activity;

import com.example.eventplanner.model.event.Activity;

import java.util.Date;

public class ActivityMapper {
    private ActivityMapper() {}
    public static ActivityDto toDto(Activity activity) {
        if (activity == null)
            return null;

        return new ActivityDto(
                activity.getName(),
                activity.getActivityStart().getTime(),
                activity.getActivityEnd().getTime(),
                activity.getDescription(),
                activity.getLocation()
        );
    }
    public static Activity toEntity(ActivityDto dto) {
        if (dto == null)
            return null;

        Activity activity = new Activity(
                dto.getName(),
                new Date(dto.getActivityStart()),
                new Date(dto.getActivityEnd()),
                dto.getDescription(),
                dto.getLocation());
        activity.setActive(true);
        return activity;
    }
}
