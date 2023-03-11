package org.example.rentapp.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FacilityDto {

    private Long id;

    private String name;

    private Double space;

    private Long addressId;

    private Long cityId;

    private String cityName;

    private Long countryId;

    private String countryCode;

    private String countryName;

    private String addressDistrict;

    private String addressStreetName;

    private String addressStreetNumber;

    private Long ownerId;

    private String ownerEmail;

    private Boolean ownerStatus;

    private String ownerPassword;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate ownerJoinDate;

}
