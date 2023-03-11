package org.example.rentapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CountryDto {

    private Long id;

    private String code;

    private String name;
}
