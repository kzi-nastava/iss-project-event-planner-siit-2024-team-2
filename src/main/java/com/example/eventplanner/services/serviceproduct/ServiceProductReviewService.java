package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.serviceproduct.serviceproductreview.*;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductReview;
import com.example.eventplanner.model.user.BaseUser;
import com.example.eventplanner.model.utils.ReviewStatus;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductRepository;
import com.example.eventplanner.repositories.serviceproduct.ServiceProductReviewRepository;
import com.example.eventplanner.repositories.user.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class ServiceProductReviewService {
    private final ServiceProductReviewRepository serviceProductReviewRepository;
    private final ServiceProductRepository serviceProductRepository;
    private final UserRepository userRepository;

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

    public ServiceProductReviewStatusDto updateStatus(Long id, ReviewStatus status) {
        return serviceProductReviewRepository.findById(id)
                .map(spr -> {
                    spr.setReviewStatus(status);
                    serviceProductReviewRepository.save(spr);
                    return new ServiceProductReviewStatusDto(id, status);
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
