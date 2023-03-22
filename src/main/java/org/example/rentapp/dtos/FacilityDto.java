package org.example.rentapp.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
public class FacilityDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private Double space;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private AddressDto addressDto;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private UserDto owner;

    @NotNull
    @Min(1)
    private Double price;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<OrderDto> orders;

}
