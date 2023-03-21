package org.example.rentapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CityDto {

    private Long id;

    private String name;

    private CountryDto countryDto;
}
