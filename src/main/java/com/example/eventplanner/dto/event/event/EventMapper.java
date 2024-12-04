package com.example.eventplanner.dto.event.event;

import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.event.Event;

public class EventMapper {
    public static EventDto toDto(Event event) {
        if (event == null)
            return null;
        return new EventDto(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getType().getId(),
                event.getMaxAttendances(),
                event.isOpen(),
                event.getLongitude(),
                event.getLatitude(),
                event.getDate(),
                event.getActivities().stream().map(Activity::getId).toList(),
                event.getBudgets().stream().map(Budget::getId).toList()
        );
    }

    public static EventNoIdDto toDtoNoId(Event event) {
        if (event == null)
            return null;
        return new EventNoIdDto(
                event.getName(),
                event.getDescription(),
                event.getType().getId(),
                event.getMaxAttendances(),
                event.isOpen(),
                event.getLongitude(),
                event.getLatitude(),
                event.getDate(),
                event.getActivities().stream().map(Activity::getId).toList(),
                event.getBudgets().stream().map(Budget::getId).toList()
        );
    }

    public static EventSummaryDto toSummaryDto(Event event) {
        if (event == null)
            return null;
        return new EventSummaryDto(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getType().getId(),
                event.getMaxAttendances(),
                event.isOpen(),
                event.getLongitude(),
                event.getLatitude(),
                event.getDate()
        );
    }

    public static Event toEntity(EventDto dto, int depth) {
        if (dto == null)
            return null;
        Event event = new Event(
                dto.getName(),
                dto.getDescription(),
                null,
                dto.getMaxAttendances(),
                dto.isOpen(),
                dto.getLongitude(),
                dto.getLatitude(),
                dto.getDate(),
                null,
                null);
        event.setId(dto.getId());
        event.setActive(true);
        return event;
    }

    public static Event toEntity(EventNoIdDto dto) {
        if (dto == null)
            return null;
        Event event = new Event(
                dto.getName(),
                dto.getDescription(),
                null,
                dto.getMaxAttendances(),
                dto.isOpen(),
                dto.getLongitude(),
                dto.getLatitude(),
                dto.getDate(),
                null,
                null);
        event.setActive(true);
        return event;
    }

}