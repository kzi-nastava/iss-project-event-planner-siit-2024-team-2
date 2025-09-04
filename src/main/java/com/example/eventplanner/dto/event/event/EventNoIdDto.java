package com.example.eventplanner.dto.event.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventNoIdDto {
    @NotBlank(message = "Name is required")
    @Length(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    @NotBlank(message = "Description is required")
    @Length(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters")
    private String description;
    @Min(value = 1, message = "Event type is required")
    private long eventTypeId;
    @Min(value = 1, message = "Event organizer is required")
    private long eventOrganizerId;
    @Min(value = 1, message = "Max attendances must be greater than 0")
    private int maxAttendances;
    private boolean open;
    private double longitude;
    private double latitude;
    @NotNull(message = "Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private List<Long> activityIds;
    private List<Long> budgetIds;
    private List<@Email String> invitationEmails;
}