package com.todo.exception.dto;

import lombok.Builder;

@Builder
public record ErrorMessage(
        String message,
        String field,
        Object invalidValue
) {
}