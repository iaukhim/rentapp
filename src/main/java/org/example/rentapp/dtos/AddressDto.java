package org.example.rentapp.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AddressDto {

    private Long id;

    @NotNull
    @Valid
    private CityDto cityDto;

    private String district;

    @NotBlank
    private String streetName;

    @NotBlank
    private String streetNumber;
}
