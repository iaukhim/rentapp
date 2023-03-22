package org.example.rentapp.exceptions.response;

import lombok.Data;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class ExceptionResponse {

    private LocalDateTime timestamp;

    @Setter
    private HttpStatus status;

    private String error;

    private String message;

    public void setException(Exception e) {
        this.timestamp = LocalDateTime.now();
        this.error = e.getClass().getSimpleName();
        this.message = e.getMessage();
        this.timestamp = LocalDateTime.now();
    }

    public void clear() {
        this.timestamp = null;
        this.status = null;
        this.message = "";
        this.error = "";
    }
}
