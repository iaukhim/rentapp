package org.example.rentapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AddressDto {

    private Long id;

    private Long cityId;

    private String cityName;

    private Long countryId;

    private String countryCode;

    private String countryName;

    private String district;

    private String streetName;

    private String streetNumber;
}
