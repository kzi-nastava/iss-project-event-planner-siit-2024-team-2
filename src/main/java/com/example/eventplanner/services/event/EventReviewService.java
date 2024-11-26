package com.example.eventplanner.services.event;

import com.example.eventplanner.model.event.EventReview;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventReviewService {
    List<EventReview> eventReviews = new ArrayList<>();
    public EventReviewService() {}
}
