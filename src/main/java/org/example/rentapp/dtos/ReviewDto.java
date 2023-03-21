package org.example.rentapp.dtos;

import lombok.Data;
import org.example.rentapp.additional.Rating;

@Data
public class ReviewDto {

    private Long id;

    private String textReview;

    private Rating rating;

    private FacilityDto facilityDto;

    private OrderDto orderDto;
}
