package com.example.eventplanner.services.serviceproduct;

import com.example.eventplanner.dto.event.eventreview.EventReviewCommentDto;
import com.example.eventplanner.dto.event.eventreview.EventReviewStatusDto;
import com.example.eventplanner.dto.serviceproduct.serviceproductreview.*;
import com.example.eventplanner.model.Entity;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductReview;
import com.example.eventplanner.model.user.User;
import com.example.eventplanner.model.utils.ReviewStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@NoArgsConstructor
public class ServiceProductReviewService {
    Map<Long, ServiceProductReview> serviceProductReviews = new HashMap<Long, ServiceProductReview>();
    private long idCounter = 0;

    public List<ServiceProductReviewDto> getAll() {
        return serviceProductReviews.values()
                .stream()
                .filter(Entity::isActive)
                .map(ServiceProductReviewMapper::toDto)
                .toList();
    }

    public ServiceProductReviewDto getById(long id) {
        if (!serviceProductReviews.containsKey(id) || !serviceProductReviews.get(id).isActive())
            return null;
        return ServiceProductReviewMapper.toDto(serviceProductReviews.get(id));
    }

    public ServiceProductReviewDto create(ServiceProductReviewNoIdDto dto) {
        ServiceProductReview serviceProductReview = ServiceProductReviewMapper.toEntity(dto);
        serviceProductReview.setId(idCounter++);
        serviceProductReview.setActive(true);

        // link user
        User testUser = new User();
        testUser.setId(dto.getUserId());
        serviceProductReview.setUser(testUser);
        // link serviceProduct
        ServiceProduct testServiceProduct = new ServiceProduct();
        testServiceProduct.setId(dto.getServiceProductId());
        serviceProductReview.setServiceProduct(testServiceProduct);

        serviceProductReviews.put(serviceProductReview.getId(), serviceProductReview);
        return ServiceProductReviewMapper.toDto(serviceProductReview);
    }

    public ServiceProductReviewDto update(ServiceProductReviewNoIdDto dto, long id) {
        if (this.getById(id) == null)
            return null;
        ServiceProductReview serviceProductReview = ServiceProductReviewMapper.toEntity(dto);

        // link user
        User testUser = new User();
        testUser.setId(dto.getUserId());
        serviceProductReview.setUser(testUser);
        // link serviceProduct
        ServiceProduct testServiceProduct = new ServiceProduct();
        testServiceProduct.setId(dto.getServiceProductId());
        serviceProductReview.setServiceProduct(testServiceProduct);

        serviceProductReviews.put(id, ServiceProductReviewMapper.toEntity(dto));
        return ServiceProductReviewMapper.toDto(serviceProductReview);
    }

    public boolean delete(long id) {
        if (this.getById(id) == null)
            return false;
        serviceProductReviews.get(id).setActive(false);
        return true;
    }

    public ServiceProductReviewStatusDto updateStatus(Long id, ReviewStatus status) {
        if (this.getById(id) == null)
            return null;
        serviceProductReviews.get(id).setReviewStatus(status);
        return new ServiceProductReviewStatusDto(id, status);
    }

    public ServiceProductReviewCommentDto updateComment(Long id, String comment) {
        if (this.getById(id) == null)
            return null;
        serviceProductReviews.get(id).setComment(comment);
        return new ServiceProductReviewCommentDto(id, comment);
    }
}
