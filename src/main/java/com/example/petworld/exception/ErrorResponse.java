package com.example.petworld.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private Map<String, String> errors = new HashMap<>();

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}