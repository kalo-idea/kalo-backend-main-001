package kalo.main.controller;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final String message;

    @Builder
    public ExceptionResponse(String message) {
        this.message = message;
    }
}
