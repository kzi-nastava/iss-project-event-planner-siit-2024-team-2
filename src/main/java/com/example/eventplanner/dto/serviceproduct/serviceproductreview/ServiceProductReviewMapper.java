package com.example.eventplanner.dto.serviceproduct.serviceproductreview;

import com.example.eventplanner.model.serviceproduct.ServiceProductReview;

public class ServiceProductReviewMapper {
    public static ServiceProductReviewDto toDto(ServiceProductReview serviceProductReview) {
        if (serviceProductReview == null)
            return null;
        return new ServiceProductReviewDto(
                serviceProductReview.getId(),
                serviceProductReview.getGrade(),
                serviceProductReview.getComment(),
                serviceProductReview.getUser().getId(),
                serviceProductReview.getServiceProduct().getId(),
                serviceProductReview.getReviewStatus()
        );
    }

    public static ServiceProductReviewNoIdDto toDtoNoId(ServiceProductReview serviceProductReview) {
        if (serviceProductReview == null)
            return null;
        return new ServiceProductReviewNoIdDto(
                serviceProductReview.getGrade(),
                serviceProductReview.getComment(),
                serviceProductReview.getUser().getId(),
                serviceProductReview.getServiceProduct().getId(),
                serviceProductReview.getReviewStatus()
        );
    }

    public static ServiceProductReview toEntity(ServiceProductReviewDto dto) {
        if (dto == null)
            return null;
        ServiceProductReview serviceProductReview = new ServiceProductReview(
                dto.getGrade(),
                dto.getComment(),
                null,
                null,
                dto.getReviewStatus());
        serviceProductReview.setId(dto.getId());
        serviceProductReview.setActive(true);
        return serviceProductReview;
    }

    public static ServiceProductReview toEntity(ServiceProductReviewNoIdDto dto) {
        if (dto == null)
            return null;
        ServiceProductReview serviceProductReview = new ServiceProductReview(
                dto.getGrade(),
                dto.getComment(),
                null,
                null,
                dto.getReviewStatus());
        serviceProductReview.setActive(true);
        return serviceProductReview;
    }
}