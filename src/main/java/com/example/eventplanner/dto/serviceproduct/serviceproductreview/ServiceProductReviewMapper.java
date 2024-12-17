package com.example.eventplanner.dto.serviceproduct.serviceproductreview;

import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductMapper;
import com.example.eventplanner.dto.user.user.UserMapper;
import com.example.eventplanner.model.serviceproduct.ServiceProduct;
import com.example.eventplanner.model.serviceproduct.ServiceProductReview;
import com.example.eventplanner.model.user.BaseUser;

public class ServiceProductReviewMapper {
    public static ServiceProductReviewDto toDto(ServiceProductReview serviceProductReview) {
        if (serviceProductReview == null)
            return null;
        return new ServiceProductReviewDto(
                serviceProductReview.getId(),
                serviceProductReview.getGrade(),
                serviceProductReview.getComment(),
                ServiceProductMapper.toDto(serviceProductReview.getServiceProduct()),
                UserMapper.toBaseUserDto(serviceProductReview.getUser()),
                serviceProductReview.getReviewStatus()
        );
    }

    public static ServiceProductReviewNoIdDto toDtoNoId(ServiceProductReview serviceProductReview) {
        if (serviceProductReview == null)
            return null;
        return new ServiceProductReviewNoIdDto(
                serviceProductReview.getGrade(),
                serviceProductReview.getComment(),
                serviceProductReview.getServiceProduct().getId(),
                serviceProductReview.getUser().getId(),
                serviceProductReview.getReviewStatus()
        );
    }

    public static ServiceProductReview toEntity(ServiceProductReviewNoIdDto dto,
                                                ServiceProduct serviceProduct,
                                                BaseUser user) {
        if (dto == null)
            return null;
        return new ServiceProductReview(
                dto.getGrade(),
                dto.getComment(),
                serviceProduct,
                user,
                dto.getReviewStatus());
    }
}