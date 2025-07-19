package com.example.eventplanner.model.event;

public interface EventCreatorProjection {
    Long getEventId();
    String getCreatorUsername();
    String getCreatorEmail();
}