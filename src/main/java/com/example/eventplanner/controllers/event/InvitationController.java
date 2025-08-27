package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.invitation.*;
import com.example.eventplanner.model.utils.InvitationResult;
import com.example.eventplanner.services.event.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor()
public class InvitationController {
    private final InvitationService invitationService;

    @GetMapping
    public ResponseEntity<Collection<InvitationDto>> getAllInvitations() {
        Collection<InvitationDto> result = invitationService.getAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InvitationDto> getInvitation(@PathVariable("id") Long id) {
        InvitationDto result = invitationService.getById(id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<InvitationDto> updateInvitation(@PathVariable("id") Long id, @RequestBody InvitationNoIdDto dto) {
        InvitationDto result = invitationService.update(dto, id);
        return result != null ?
                new ResponseEntity<>(result, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/{token}/accept")
    public ResponseEntity<?> acceptInvitation(@PathVariable("token") String token) {
        InvitationResult result = invitationService.acceptInvitation(token);
        if (result.getError() == null)
            return new ResponseEntity<>(InvitationMapper.toDto(result.getInvitation()), HttpStatus.OK);
        else {
            InvitationErrorDto errorDto = new InvitationErrorDto(result.getError());
            if (result.getInvitation() != null && result.getInvitation().getEvent() != null)
                errorDto.setEventId(result.getInvitation().getEvent().getId());

            return switch (result.getError()) {
                case EVENT_FULL, EVENT_FULL_QUICK_REGISTRATION -> new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
                case UNAUTHORIZED_QUICK_REGISTRATION, UNAUTHORIZED -> new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
                case FORBIDDEN -> new ResponseEntity<>(errorDto, HttpStatus.FORBIDDEN);
                case INVITATION_NOT_FOUND, EVENT_NOT_FOUND -> new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
            };
        }
    }
}
