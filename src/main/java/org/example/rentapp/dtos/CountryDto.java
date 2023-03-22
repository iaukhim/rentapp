package org.example.rentapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CountryDto {

    private Long id;

    @NotBlank
    private String code;

    @NotBlank
    private String name;
}
