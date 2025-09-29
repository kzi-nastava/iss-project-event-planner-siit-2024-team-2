package com.example.eventplanner.dto.event.event;

import com.example.eventplanner.dto.event.activity.ActivityMapper;
import com.example.eventplanner.dto.event.budget.BudgetMapper;
import com.example.eventplanner.dto.event.eventtype.EventTypeMapper;
import com.example.eventplanner.dto.event.invitation.InvitationMapper;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.event.*;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.user.EventOrganizer;
import com.example.eventplanner.services.serviceproduct.ImageService;
import com.example.eventplanner.services.util.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventMapper {
    private EventMapper() {}

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
                event.getBudgets().stream().map(BudgetMapper::toDto).toList(),
                event.getInvitations().stream().map(Invitation::getEmail).toList()
        );
    }

    public static EventNoIdDto toDtoNoId(Event event) {
        if (event == null)
            return null;

        return new EventNoIdDto(
                event.getName(),
                event.getDescription(),
                event.getType().getId(),
                event.getEventOrganizer().getId(),
                event.getMaxAttendances(),
                event.isOpen(),
                event.getLongitude(),
                event.getLatitude(),
                event.getDate(),
                event.getActivities().stream().map(Activity::getId).toList(),
                event.getBudgets().stream().map(Budget::getId).toList(),
                event.getInvitations() != null
                        ? event.getInvitations().stream().map(Invitation::getEmail).toList()
                        : new ArrayList<>()
        );
    }

    public static EventSummaryDto toSummaryDto(Event event) {
        if (event == null)
            return null;
        EventOrganizer organizer = event.getEventOrganizer();

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
                organizer != null ? organizer.getFirstName() + " " + organizer.getLastName() : null,
                organizer != null ? organizer.getEmail() : null,
                ImageService.encodePath(organizer != null ? organizer.getImage() : null)
        );
    }

    public static Event toEntity(EventNoIdDto dto,
                                 EventType eventType,
                                 EventOrganizer eventOrganizer,
                                 List<Activity> activities,
                                 List<Budget> budgets,
                                 List<Invitation> invitations,
                                 List<BaseUser> attendees) {
        if (dto == null)
            return null;

        return new Event(
                dto.getName(),
                dto.getDescription(),
                eventType,
                eventOrganizer,
                dto.getMaxAttendances(),
                dto.isOpen(),
                dto.getLongitude(),
                dto.getLatitude(),
                dto.getDate(),
                activities,
                budgets,
                invitations,
                attendees
        );
    }

    public static Event toEntity(EventNoIdDto dto, EventType eventType, EventOrganizer eventOrganizer) {
        return toEntity(dto, eventType, eventOrganizer, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

}