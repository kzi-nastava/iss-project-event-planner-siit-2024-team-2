package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.communication.notification.NotificationNoIdDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductreview.*;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductReview;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.ReviewStatus;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductReviewRepository;
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
public class ServiceProductReviewService {
    private final ServiceProductReviewRepository serviceProductReviewRepository;
    private final ServiceProductRepository serviceProductRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public List<ServiceProductReviewDto> getAll() {
        return serviceProductReviewRepository.findAll()
                .stream()
                .map(ServiceProductReviewMapper::toDto)
                .toList();
    }

    public ServiceProductReviewDto getById(long id) {
        return serviceProductReviewRepository.findById(id)
                .map(ServiceProductReviewMapper::toDto)
                .orElse(null);
    }

    public ServiceProductReviewDto create(ServiceProductReviewNoIdDto dto) {
        ServiceProduct serviceProduct = serviceProductRepository.getReferenceById(dto.getServiceProductId());
        BaseUser user = userRepository.getReferenceById(dto.getUserId());

        ServiceProductReview serviceProductReview = ServiceProductReviewMapper.toEntity(dto, serviceProduct, user);
        serviceProductReviewRepository.save(serviceProductReview);
        return ServiceProductReviewMapper.toDto(serviceProductReview);
    }

    public ServiceProductReviewDto update(ServiceProductReviewNoIdDto dto, long id) {
        return serviceProductReviewRepository.findById(id)
                .map(spr -> {
                    spr.setReviewStatus(dto.getReviewStatus());
                    spr.setComment(dto.getComment());
                    spr.setGrade(dto.getGrade());
                    BaseUser user = userRepository.getReferenceById(dto.getUserId());
                    ServiceProduct serviceProduct = serviceProductRepository.getReferenceById(dto.getServiceProductId());
                    spr.setUser(user);
                    spr.setServiceProduct(serviceProduct);
                    return ServiceProductReviewMapper.toDto(serviceProductReviewRepository.save(spr));
                })
                .orElse(null);
    }

    public boolean delete(long id) {
        if (!serviceProductReviewRepository.existsById(id))
            return false;
        serviceProductReviewRepository.deleteById(id);
        return true;
    }

    public ServiceProductReviewStatusDto approve(Long id) {
        return serviceProductReviewRepository.findById(id)
                .map(spr -> {
                    spr.setReviewStatus(ReviewStatus.APPROVED);
                    String reviewerName = spr.getUser().getFirstName() + " " + spr.getUser().getLastName();
                    String notificationMessage = "*{0}* has left a review on *{1}* with a rating of **{2}/5**.\n**Review:** \n{3}";
                    notificationMessage = MessageFormat.format(notificationMessage, reviewerName, spr.getServiceProduct().getName(), spr.getGrade(), spr.getComment());
                    notificationService.sendNotification(new NotificationNoIdDto(
                            "New review for **" + spr.getServiceProduct().getName() + "**",
                            notificationMessage,
                            spr.getServiceProduct().getServiceProductProvider().getId()
                    ));
                    serviceProductReviewRepository.save(spr);
                    return new ServiceProductReviewStatusDto(id, ReviewStatus.APPROVED);
                })
                .orElse(null);
    }

    public ServiceProductReviewCommentDto updateComment(Long id, String comment) {
        return serviceProductReviewRepository.findById(id)
                .map(spr -> {
                    spr.setComment(comment);
                    serviceProductReviewRepository.save(spr);
                    return new ServiceProductReviewCommentDto(id, comment);
                })
                .orElse(null);
    }

    public Page<ServiceProductReviewDto> getAllPending(Pageable pageable) {
        return serviceProductReviewRepository.findAllByReviewStatus(ReviewStatus.PENDING, pageable)
                .map(ServiceProductReviewMapper::toDto);
    }
}
