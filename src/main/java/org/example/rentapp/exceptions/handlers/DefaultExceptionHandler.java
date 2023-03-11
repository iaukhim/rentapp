package org.example.rentapp.exceptions.handlers;

import lombok.extern.java.Log;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Level;

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
        log.error(e.getMessage());
        exceptionResponse.setException(e);
        exceptionResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
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
}