package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.event.FavouriteEventsDto;
import com.example.eventplanner.services.event.FavouriteEventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favourite-events")
@RequiredArgsConstructor()
public class FavouriteEventsController {
    private final FavouriteEventsService favouriteEventsService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<FavouriteEventsDto> getFavouriteEventsByOrganizerId(@PathVariable("id") Long id) {
        FavouriteEventsDto eventsDto = favouriteEventsService.getByOrganizerId(id);
        if (eventsDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventsDto);
    }

    @PostMapping()
    public ResponseEntity<FavouriteEventsDto> createFavouriteEvents(@RequestBody FavouriteEventsDto eventsDto) {
        return ResponseEntity.ok(favouriteEventsService.create(eventsDto));
    }

    @PutMapping()
    public ResponseEntity<FavouriteEventsDto> updateFavouriteEvents(@RequestParam(value = "organizerId") Long organizerId,
                                                                    @RequestParam(value = "eventId") Long eventId) {
        FavouriteEventsDto eventsDto = favouriteEventsService.update(organizerId, eventId);
        if (eventsDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(eventsDto);
    }
}
