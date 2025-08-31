package com.example.eventplanner.dto.order.review;

import com.example.eventplanner.dto.event.event.EventDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventReviewDto extends ReviewDto {
    private EventDto event;

    public EventReviewDto(ReviewDto dto, EventDto event) {
        super(dto.getId(), dto.getGrade(), dto.getComment(), dto.getUser(), dto.getReviewStatus(), dto.getCreatedAt());
        this.event = event;
    }
}
