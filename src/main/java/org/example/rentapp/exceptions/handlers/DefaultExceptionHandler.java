package org.example.rentapp.exceptions.handlers;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.example.rentapp.exceptions.JwtAuthenticationException;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.exceptions.UserAlreadyExists;
import org.example.rentapp.exceptions.response.ExceptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    @Autowired
    private ExceptionResponse exceptionResponse;

    @ExceptionHandler({NoEntityFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleException(NoEntityFoundException e) {
        exceptionResponse.clear();
        log.warn(e.getMessage());
        exceptionResponse.setException(e);
        exceptionResponse.setStatus(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException e) {
        exceptionResponse.clear();
        log.warn(e.getMessage());
        exceptionResponse.setException(e);
        exceptionResponse.setStatus(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ExceptionResponse> handleException(UserAlreadyExists e) {
        exceptionResponse.clear();
        log.warn(e.getMessage());
        exceptionResponse.setException(e);
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        exceptionResponse.clear();
        log.error(e.getClass().toString() + " " + e.getMessage());
        exceptionResponse.setMessage("Oops, something went wrong on server");
        exceptionResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException e) {
        exceptionResponse.clear();
        log.warn(e.getMessage());
        exceptionResponse.setException(e);
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleJwtAuthException(JwtAuthenticationException e) {
        exceptionResponse.clear();
        log.warn(e.getMessage());
        exceptionResponse.setException(e);
        exceptionResponse.setStatus(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e) {
        exceptionResponse.clear();
        log.warn(e.getMessage());
        exceptionResponse.setException(e);
        exceptionResponse.setStatus(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        exceptionResponse.clear();
        log.warn(e.getMessage());
        exceptionResponse.setException(e);
        exceptionResponse.setMessage("Check your request param types");
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        exceptionResponse.clear();
        log.warn(e.getMessage());
        exceptionResponse.setException(e);
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError ->
                        fieldError.getField() + " : " + fieldError.getDefaultMessage()
                ).collect(Collectors.toList());
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setMessage(errors.toString());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}