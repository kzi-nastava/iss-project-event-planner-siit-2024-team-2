package com.example.eventplanner.controllers.user;

import com.example.eventplanner.dto.user.user.RegisterEventOrganizerDto;
import com.example.eventplanner.dto.user.user.UpdateEventOrganizerDto;
import com.example.eventplanner.services.user.EventOrganizerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/event-organizers")
@Validated
public class EventOrganizerController {
    private final EventOrganizerService eventOrganizerService;

    @GetMapping("/{id}")
    public ResponseEntity<RegisterEventOrganizerDto> getEventOrganizerById(@PathVariable long id) {
        RegisterEventOrganizerDto registerEventOrganizerDto = eventOrganizerService.getEventOrganizerById(id);
        return registerEventOrganizerDto != null ?
                ResponseEntity.ok(registerEventOrganizerDto) :
                ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateEventOrganizerDto> updateEventOrganizer(@PathVariable long id, @Valid @RequestBody UpdateEventOrganizerDto eventOrganizerDto) {
        UpdateEventOrganizerDto user = eventOrganizerService.updateEventOrganizer(id, eventOrganizerDto);
        return user != null ?
                ResponseEntity.ok(eventOrganizerDto) :
                ResponseEntity.notFound().build();
    }
}
