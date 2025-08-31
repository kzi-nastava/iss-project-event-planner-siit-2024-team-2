package com.example.eventplanner.services.user;

import com.example.eventplanner.dto.event.event.EventDto;
import com.example.eventplanner.dto.event.event.EventMapper;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteEventsService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    public List<EventDto> getFavorites(Long userId) {
        Optional<BaseUser> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return null;

        BaseUser user = userOpt.get();
        return user.getFavoriteEvents()
                .stream()
                .map(EventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean addFavorite(Long userId, Long eventId) {
        Optional<BaseUser> userOpt = userRepository.findById(userId);
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (userOpt.isEmpty() || eventOpt.isEmpty()) return false;

        BaseUser user = userOpt.get();
        Event event = eventOpt.get();

        if (!user.getFavoriteEvents().contains(event)) {
            user.getFavoriteEvents().add(event);
            userRepository.save(user);
        }
        return true;
    }

    @Transactional
    public boolean removeFavorite(Long userId, Long eventId) {
        Optional<BaseUser> userOpt = userRepository.findById(userId);
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (userOpt.isEmpty() || eventOpt.isEmpty()) return false;

        BaseUser user = userOpt.get();
        Event event = eventOpt.get();

        if (user.getFavoriteEvents().contains(event)) {
            user.getFavoriteEvents().remove(event);
            userRepository.save(user);
        }
        return true;
    }
}
