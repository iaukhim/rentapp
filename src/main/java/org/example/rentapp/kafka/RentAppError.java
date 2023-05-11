package org.example.rentapp.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentAppError {

    private String fullyClassifiedName;

    private String causeFullName;

    private String stackTrace;

    private LocalDateTime timeStamp;

    public static RentAppError fromException (RuntimeException exception){
        StringBuilder stringBuilder = new StringBuilder();

        Arrays.stream(exception.getStackTrace()).toList().forEach(stackTraceElement -> stringBuilder.append(stackTraceElement.toString() + "\n"));
        return RentAppError.builder()
                    .fullyClassifiedName(exception.getClass().getName())
                    .causeFullName(exception.getCause().getClass().getName())
                    .timeStamp(LocalDateTime.now())
                    .stackTrace(stringBuilder.toString())
                    .build();
    }
}
