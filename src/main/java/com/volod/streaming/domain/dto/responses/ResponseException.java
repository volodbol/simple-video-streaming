package com.volod.streaming.domain.dto.responses;

import org.springframework.http.HttpStatus;

public record ResponseException(
        String message,
        int code
) {

    public static ResponseException of(String message, HttpStatus code) {
        return new ResponseException(message, code.value());
    }

}
