package com.example.eventplanner.dto.event.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteEventsDto {
    private Long organizerId;
    private List<Long> favouriteEventIds;
}
