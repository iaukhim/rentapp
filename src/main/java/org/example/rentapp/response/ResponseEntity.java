package org.example.rentapp.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@ToString
public class ResponseEntity {

    private HttpStatus httpStatus;
    @Getter
    private String responseBody;

}
