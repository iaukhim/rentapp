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
public class CityDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Valid
    private CountryDto countryDto;
}
