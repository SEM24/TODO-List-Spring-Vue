package com.todo.auth.model.dto;

import lombok.Builder;
import org.springframework.http.ResponseCookie;

//TODO validate all fields with annotations
@Builder
public record LogoutResponse(String message,
                             ResponseCookie accessTokenCookie,
                             ResponseCookie refreshTokenCookie) {
}
