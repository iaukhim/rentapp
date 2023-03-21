package org.example.rentapp.dtos;

import org.example.rentapp.additional.Rating;

public class ReviewDto {

    private Long id;

    private String textReview;

    private Rating rating;


    private FacilityDto facilityDto;

    private OrderDto orderDto;
}
