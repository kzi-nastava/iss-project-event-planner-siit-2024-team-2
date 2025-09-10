package com.example.eventplanner.dto;

import com.example.eventplanner.dto.order.booking.BookingReminderDto;
import lombok.AllArgsConstructor;

import java.util.Date;

@AllArgsConstructor
public class TestBookingReminderDto implements BookingReminderDto {
    private final Long bookingId;
    private final String serviceName;
    private final String eventName;
    private final Long organizerId;
    private final Date startTime;

    @Override public Long getBookingId() { return bookingId; }
    @Override public String getServiceName() { return serviceName; }
    @Override public String getEventName() { return eventName; }
    @Override public Long getOrganizerId() { return organizerId; }
    @Override public Date getStartTime() { return startTime; }
}