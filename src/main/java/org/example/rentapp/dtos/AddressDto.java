package org.example.rentapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AddressDto {

    private Long id;

    private CityDto cityDto;

    private String district;

    private String streetName;

    private String streetNumber;
}
