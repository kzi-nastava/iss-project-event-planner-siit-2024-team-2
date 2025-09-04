package com.example.eventplanner.dto.order.review;

import com.example.eventplanner.dto.serviceproduct.serviceproduct.ServiceProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProductReviewDto extends ReviewDto {
    private ServiceProductDto serviceProduct;

    public ServiceProductReviewDto(ReviewDto dto, ServiceProductDto serviceProduct) {
        super(dto.getId(), dto.getGrade(), dto.getComment(), dto.getUser(), dto.getReviewStatus(), dto.getCreatedAt());
        this.serviceProduct = serviceProduct;
    }
}
