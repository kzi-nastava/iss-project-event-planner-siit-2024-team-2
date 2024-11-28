package com.example.eventplanner.services.event;

import com.example.eventplanner.dto.event.FavouriteEventsDto;
import com.example.eventplanner.dto.serviceproduct.CreateServiceDto;
import com.example.eventplanner.dto.serviceproduct.ServiceDto;
import com.example.eventplanner.dto.serviceproduct.ServiceMapper;
import com.example.eventplanner.model.Entity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.spi.ToolProvider.findFirst;

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
