package com.example.petworld.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseTemplate<T> {
    private int status;
    private String message;
    private String error;
    private T data;
}
