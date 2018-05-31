package com.example.uploadform.api;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


@Value
@AllArgsConstructor
public class ErrorResponse {

    private final HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    public ErrorResponse(HttpStatus status, Throwable ex) {
        this(status, LocalDateTime.now(), "Unexpected error", ex.getLocalizedMessage());
    }

    public ErrorResponse(HttpStatus status, String message, Throwable ex) {
        this(status, LocalDateTime.now(), message, ex.getLocalizedMessage());
    }

}