package com.example.eventplanner.controllers.event;

import com.example.eventplanner.dto.event.invitation.InvitationDto;
import com.example.eventplanner.dto.event.invitation.InvitationNoIdDto;
import com.example.eventplanner.services.event.InvitationService;
import com.example.eventplanner.utils.StatusPair;
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
    public ResponseEntity<InvitationDto> acceptInvitation(@PathVariable("token") String token) {
        StatusPair<InvitationDto> result = invitationService.acceptInvitation(token);
        return result.getStatus() == HttpStatus.OK ?
                new ResponseEntity<>(result.getValue(), HttpStatus.OK) :
                new ResponseEntity<>(result.getStatus());
    }
}
