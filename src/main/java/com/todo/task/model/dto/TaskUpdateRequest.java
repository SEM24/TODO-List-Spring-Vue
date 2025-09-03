package com.todo.task.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.todo.task.model.enitity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskUpdateRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,
        Priority priority,

        Boolean completed,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")

        LocalDateTime dueDate
) {
}
