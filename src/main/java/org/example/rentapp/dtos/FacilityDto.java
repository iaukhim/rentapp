package org.example.rentapp.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FacilityDto {

    private Long id;

    private String name;

    private Double space;

    private AddressDto addressDto;

    private UserDto owner;

}
