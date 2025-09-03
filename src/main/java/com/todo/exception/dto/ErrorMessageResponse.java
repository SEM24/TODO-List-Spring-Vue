package com.todo.exception.dto;

import com.todo.exception.MessageType;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@Builder
public record ErrorMessageResponse(Instant createdAt, MessageType type,
                                   HttpStatus statusCode, int status,
                                   String message, Map<String, Object> details) {
}