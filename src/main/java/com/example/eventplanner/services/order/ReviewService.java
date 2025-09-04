package com.example.eventplanner.services.order;

import com.example.eventplanner.controllers.utils.AuthUtil;
import com.example.eventplanner.dto.communication.notification.NotificationNoIdDto;
import com.example.eventplanner.dto.order.review.*;
import com.example.eventplanner.exception.ForbiddenException;
import com.example.eventplanner.exception.UnauthorizedException;
import com.example.eventplanner.model.event.Event;
import com.example.eventplanner.model.order.EventReview;
import com.example.eventplanner.model.order.Review;
import com.example.eventplanner.model.serviceproduct.Product;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.order.ServiceProductReview;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.ReviewStatus;
import com.example.eventplanner.model.utils.ReviewType;
import com.example.eventplanner.model.utils.ServiceProductDType;
import com.example.eventplanner.model.utils.UserRole;
import com.example.eventplanner.repositories.event.EventRepository;
import com.example.eventplanner.repositories.order.EventReviewRepository;
import com.example.eventplanner.repositories.order.ServiceProductReviewRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import com.example.eventplanner.repositories.order.ReviewRepository;
import com.example.eventplanner.repositories.user.EventOrganizerRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import com.example.eventplanner.services.communication.NotificationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ServiceProductRepository serviceProductRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final EventRepository eventRepository;
    private final AuthUtil authUtil;
    private final EventReviewRepository eventReviewRepository;
    private final ServiceProductReviewRepository serviceProductReviewRepository;
    private final EventOrganizerRepository eventOrganizerRepository;

    public List<ReviewDto> getAll() {
        return reviewRepository.findAll()
                .stream()
                .map(ReviewMapper::toDto)
                .toList();
    }

    public ReviewDto getById(long id) {
        return reviewRepository.findById(id)
                .map(ReviewMapper::toDto)
                .orElse(null);
    }

    public ReviewDto create(ReviewNoIdDto dto) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            throw new UnauthorizedException("User not authenticated");
        ServiceProduct serviceProduct = null;
        Event event = null;
        if (dto.getReviewType() == ReviewType.SERVICE_PRODUCT) {
            serviceProduct = serviceProductRepository.getReferenceById(dto.getEntityId());
            ReviewEligibilityDto eligibility = canReviewServiceProduct(serviceProduct.getId());
            if (!eligibility.isCanReview())
                throw new ForbiddenException(eligibility.getReason());
        }
        else if (dto.getReviewType() == ReviewType.EVENT) {
            event = eventRepository.getReferenceById(dto.getEntityId());
            ReviewEligibilityDto eligibility = canReviewEvent(event.getId());
            if (!eligibility.isCanReview())
                throw new ForbiddenException(eligibility.getReason());
        }

        Review review = ReviewMapper.toEntity(dto, serviceProduct, event, user, ReviewStatus.PENDING);
        reviewRepository.save(review);
        return ReviewMapper.toDto(review);
    }

    public ReviewDto update(ReviewNoIdDto dto, long id) {
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setComment(dto.getComment());
                    review.setGrade(dto.getGrade());
                    if (dto.getReviewType() == ReviewType.SERVICE_PRODUCT) {
                        ServiceProduct serviceProduct = serviceProductRepository.getReferenceById(dto.getEntityId());
                        ServiceProductReview spr = (ServiceProductReview) review;
                        spr.setServiceProduct(serviceProduct);
                        return ReviewMapper.toDto(reviewRepository.save(spr));
                    }
                    else if (dto.getReviewType() == ReviewType.EVENT) {
                        Event event = eventRepository.getReferenceById(dto.getEntityId());
                        EventReview er = (EventReview) review;
                        er.setEvent(event);
                        return ReviewMapper.toDto(reviewRepository.save(er));
                    }
                    else
                        return null;
                })
                .orElse(null);
    }

    public boolean delete(long id) {
        if (!reviewRepository.existsById(id))
            return false;
        reviewRepository.deleteById(id);
        return true;
    }

    public ReviewStatusDto approve(Long id) {
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setReviewStatus(ReviewStatus.APPROVED);
                    String entityName = "";
                    String entityType = "";
                    long entityUserId = 0;
                    if (review instanceof ServiceProductReview){
                        ServiceProduct serviceProduct = ((ServiceProductReview) review).getServiceProduct();
                        entityName = serviceProduct.getName();
                        entityType = serviceProduct instanceof Product ? "product" : "service";
                        entityUserId = serviceProduct.getServiceProductProvider().getId();
                    }
                    else if (review instanceof EventReview) {
                        Event event = ((EventReview) review).getEvent();
                        entityName = event.getName();
                        entityType = "event";
                        entityUserId = event.getEventOrganizer().getId();
                    }

                    String reviewerName = review.getUser().getFirstName() + " " + review.getUser().getLastName();
                    String notificationMessage = "*{0}* has left a review on {1} *{2}* with a rating of **{3}/5**.\n**Review:** \n{4}";
                    notificationMessage = MessageFormat.format(notificationMessage, reviewerName, entityType, entityName, review.getGrade(), review.getComment());
                    notificationService.sendNotification(new NotificationNoIdDto(
                            "New review for " + entityType + " **" + entityName + "**",
                            notificationMessage,
                            entityUserId
                    ));
                    reviewRepository.save(review);
                    return new ReviewStatusDto(id, ReviewStatus.APPROVED);
                })
                .orElse(null);
    }

    public ReviewCommentDto updateComment(Long id, String comment) {
        return reviewRepository.findById(id)
                .map(spr -> {
                    spr.setComment(comment);
                    reviewRepository.save(spr);
                    return new ReviewCommentDto(id, comment);
                })
                .orElse(null);
    }

    public Page<ReviewDto> getAllPending(Pageable pageable) {
        return reviewRepository.findAllByReviewStatus(ReviewStatus.PENDING, pageable)
                .map(ReviewMapper::toDto);
    }

    public ReviewEligibilityDto canReviewEvent(Long eventId) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return new ReviewEligibilityDto(false, "You are not logged in");
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null)
            return new ReviewEligibilityDto(false, "Event not found");
        if (event.getEventOrganizer() == null)
            return new ReviewEligibilityDto(false, "Event organizer was deleted");
        if (event.getEventOrganizer().getId() == user.getId())
            return new ReviewEligibilityDto(false, "You can't review your own event");
        Review review = eventReviewRepository.findFirstByUserIdAndEventId(user.getId(), eventId).orElse(null);
        if (review != null)
            return new ReviewEligibilityDto(false, "You have already reviewed this event");
        if (event.getAttendees().stream().noneMatch(attendee -> attendee.getId() == user.getId()))
            return new ReviewEligibilityDto(false, "You are not attending this event");
        return new ReviewEligibilityDto(true, "");
    }

    public ReviewEligibilityDto canReviewServiceProduct(Long serviceProductId) {
        BaseUser user = authUtil.getAuthenticatedUser();
        if (user == null)
            return new ReviewEligibilityDto(false, "You are not logged in");
        ServiceProduct serviceProduct = serviceProductRepository.findById(serviceProductId).orElse(null);
        System.out.println(serviceProduct);
        if (serviceProduct == null)
            return new ReviewEligibilityDto(false, "Service/product not found");
        if (user.getUserRole() != UserRole.EVENT_ORGANIZER)
            return new ReviewEligibilityDto(false, "Only event organizers can review services/products");
        if (serviceProduct.getServiceProductProvider() == null)
            return new ReviewEligibilityDto(false, "Service/product provider was deleted");
        if (serviceProduct.getServiceProductProvider().getId() == user.getId())
            return new ReviewEligibilityDto(false, "You can't review your own service/product");
        Review review = serviceProductReviewRepository.findFirstByUserIdAndServiceProductId(user.getId(), serviceProductId).orElse(null);
        if (review != null)
            return new ReviewEligibilityDto(false, "You have already reviewed this service/product");
        if (serviceProduct.getDtype().equals("Product")) {
            boolean hasPurchased = eventOrganizerRepository.hasPurchased(user.getId(), serviceProductId);
            if (!hasPurchased)
                return new ReviewEligibilityDto(false, "You haven't purchased this product");
        } else {
            boolean hasBooked = eventOrganizerRepository.hasBooked(user.getId(), serviceProductId);
            if (!hasBooked)
                return new ReviewEligibilityDto(false, "You haven't booked this service");
        }
        return new ReviewEligibilityDto(true, "");
    }
}
