package org.example.rentapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.rentapp.additional.Rating;

@Data
public class ReviewDto {

    private Long id;

    @NotBlank
    @Size(min = 20, max = 1000)
    private String textReview;

    @NotNull
    private Rating rating;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private FacilityDto facilityDto;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private OrderDto orderDto;
}
