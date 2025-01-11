package com.volod.streaming.controllers;

import com.volod.streaming.dto.responses.ResponseException;
import com.volod.streaming.exceptions.VideoNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@Order
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseException> handleException(Exception ex) {
        log.error("Unknown error occurred", ex);
        return new ResponseEntity<>(
                ResponseException.of("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({
            VideoNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseException> handleNotFoundException(Exception ex) {

        return new ResponseEntity<>(
                ResponseException.of(ex.getMessage(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }

}
