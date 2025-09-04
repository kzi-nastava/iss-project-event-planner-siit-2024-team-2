package com.example.eventplanner.dto.order.booking;

import java.util.Date;

public interface BookingReminderDto {
    Long getBookingId();
    Long getOrganizerId();
    String getEventName();
    String getServiceName();
    Date getStartTime();
}
