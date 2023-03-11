package org.example.rentapp.response;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactory {

    public ResponseEntity getEntity(HttpStatus status, String body) {
        return new ResponseEntity(status, body);
    }

    public ResponseEntity getEntity(HttpStatus status) {
        return new ResponseEntity(status, "");
    }
}
