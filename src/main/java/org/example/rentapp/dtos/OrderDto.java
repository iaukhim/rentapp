package org.example.rentapp.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class OrderDto {

    private Long id;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Future
    @NotNull
    private LocalDate plannedDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @PastOrPresent
    @NotNull
    private LocalDate creationDate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private UserDto renter;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    private FacilityDto facility;

    @NotNull
    private Long durationInDays;
}
