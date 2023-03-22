package org.example.rentapp.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @Email
    @NotNull
    private String email;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotEmpty
    @Valid
    private List<RoleDto> roles;

    @NotNull
    private Boolean status;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull
    @PastOrPresent
    private LocalDate joindate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private AddressDto addressDto;

    private String phoneNumber;

    private String firstName;

    private String lastName;


}
