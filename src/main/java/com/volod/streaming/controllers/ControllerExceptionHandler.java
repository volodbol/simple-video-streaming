package com.volod.streaming.controllers;

import com.volod.streaming.domain.dto.responses.ResponseException;
import com.volod.streaming.domain.exceptions.VideoEngagementNotFoundException;
import com.volod.streaming.domain.exceptions.VideoNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseException> handleMessageNotReadableException(HttpMessageNotReadableException ignored) {
        return new ResponseEntity<>(
                ResponseException.of("Malformed request body", HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseException> handleMethodValidationException(HandlerMethodValidationException ex) {
        var message = ex.getAllErrors().stream()
                .filter(error -> nonNull(error.getCodes()) && error.getCodes().length > 0)
                .map(error -> {
                    var code = error.getCodes()[0];
                    var fieldName = code.substring(code.lastIndexOf(".") + 1);
                    return fieldName + ": " + error.getDefaultMessage();
                })
                .sorted()
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(
                ResponseException.of(message, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseException> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var message = ex.getAllErrors().stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(
                ResponseException.of(message, HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({
            VideoNotFoundException.class,
            VideoEngagementNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseException> handleNotFoundException(Exception ex) {
        return new ResponseEntity<>(
                ResponseException.of(ex.getMessage(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }

}
