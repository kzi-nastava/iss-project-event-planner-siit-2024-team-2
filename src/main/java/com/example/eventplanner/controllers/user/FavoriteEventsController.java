package com.example.eventplanner.controllers.user;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventSummaryDto;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.services.user.FavoriteEventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/users/{userId}/favorite-events")
@RequiredArgsConstructor
public class FavoriteEventsController {

    private final FavoriteEventsService favoriteEventsService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<Collection<EventSummaryDto>> getFavorites(@PathVariable long userId) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.getId() != userId && user.getUserRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Collection<EventSummaryDto> favorites = favoriteEventsService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<Void> addFavorite(@PathVariable long userId, @PathVariable long eventId) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.getId() != userId && user.getUserRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean success = favoriteEventsService.addFavorite(userId, eventId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable long userId, @PathVariable long eventId) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.getId() != userId && user.getUserRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean success = favoriteEventsService.removeFavorite(userId, eventId);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
