package com.example.eventplanner.controllers.user;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductSummaryDto;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.services.user.FavoriteServiceProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/users/{userId}/favorite-service-products")
@RequiredArgsConstructor
public class FavoriteServiceProductsController {

    private final FavoriteServiceProductsService favoriteServiceProductsService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<Collection<ServiceProductSummaryDto>> getFavorites(@PathVariable long userId) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.getId() != userId && user.getUserRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Collection<ServiceProductSummaryDto> favorites = favoriteServiceProductsService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{serviceProductId}")
    public ResponseEntity<Void> addFavorite(@PathVariable long userId, @PathVariable long serviceProductId) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.getId() != userId && user.getUserRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean success = favoriteServiceProductsService.addFavorite(userId, serviceProductId);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{serviceProductId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable long userId, @PathVariable long serviceProductId) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (user.getId() != userId && user.getUserRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean success = favoriteServiceProductsService.removeFavorite(userId, serviceProductId);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
