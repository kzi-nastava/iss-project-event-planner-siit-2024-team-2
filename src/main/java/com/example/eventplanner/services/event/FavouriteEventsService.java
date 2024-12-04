package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.event.FavouriteEventsDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavouriteEventsService {
    private final List<FavouriteEventsDto> favouriteEventsDtoList = new ArrayList<>();

    public FavouriteEventsDto getByOrganizerId(Long id) {
        for (FavouriteEventsDto eventDto : favouriteEventsDtoList) {
            if (eventDto.getOrganizerId().equals(id)) {
                return eventDto;
            }
        }
        return null;
    }

    public FavouriteEventsDto create(FavouriteEventsDto dto) {
        favouriteEventsDtoList.add(dto);
        return dto;
    }

    public FavouriteEventsDto update(Long organizerId, Long eventId) {
        FavouriteEventsDto favouriteEventsDto = getByOrganizerId(organizerId);
        if (favouriteEventsDto == null) {
            return null;
        }
        favouriteEventsDto.getFavouriteEventIds().add(eventId);
        return favouriteEventsDto;
    }
}
