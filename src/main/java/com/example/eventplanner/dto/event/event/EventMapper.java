package com.example.eventplanner.dto.event.event;

import com.example.eventplanner.dto.event.activity.ActivityMapper;
import com.example.eventplanner.dto.event.budget.BudgetMapper;
import com.example.eventplanner.dto.event.eventtype.EventTypeMapper;
import com.example.eventplanner.dto.user.user.EventOrganizerMapper;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.event.Activity;
import com.example.eventplanner.model.event.Budget;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.event.EventType;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.services.util.DateUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class EventMapper {
    public static EventDto toDto(Event event) {
        if (event == null)
            return null;
        return new EventDto(
                event.getId(),
                event.getName(),
                event.getDescription(),
                EventTypeMapper.toDto(event.getType()),
                UserMapper.toBaseUserDto(event.getEventOrganizer()),
                event.getMaxAttendances(),
                event.isOpen(),
                event.getLongitude(),
                event.getLatitude(),
                event.getDate().getTime(),
                event.getActivities().stream().map(ActivityMapper::toDto).toList(),
                event.getBudgets().stream().map(BudgetMapper::toDto).toList()
        );
    }

    public static EventNoIdDto toDtoNoId(Event event) {
        if (event == null)
            return null;
        LocalDate localDate = DateUtil.convertDateToLocalDate(event.getDate());
        return new EventNoIdDto(
                event.getName(),
                event.getDescription(),
                event.getType().getId(),
                event.getEventOrganizer().getId(),
                event.getMaxAttendances(),
                event.isOpen(),
                event.getLongitude(),
                event.getLatitude(),
                localDate,
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
                EventTypeMapper.toDto(event.getType()),
                event.getMaxAttendances(),
                event.isOpen(),
                event.getLongitude(),
                event.getLatitude(),
                event.getDate().getTime(),
                event.getEventOrganizer().getFirstName() + " " + event.getEventOrganizer().getLastName(),
                event.getEventOrganizer().getEmail()
        );
    }

    public static Event toEntity(EventNoIdDto dto, EventType eventType, EventOrganizer eventOrganizer, List<Activity> activities, List<Budget> budgets) {
        if (dto == null)
            return null;
        Date convertedDate = DateUtil.convertLocalDateToDate(dto.getDate());
        return new Event(
                dto.getName(),
                dto.getDescription(),
                eventType,
                eventOrganizer,
                dto.getMaxAttendances(),
                dto.isOpen(),
                dto.getLongitude(),
                dto.getLatitude(),
                convertedDate,
                activities,
                budgets);
    }

}