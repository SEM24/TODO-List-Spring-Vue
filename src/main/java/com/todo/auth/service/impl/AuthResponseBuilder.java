package com.todo.auth.service.impl;

import com.todo.auth.model.dto.AuthResponse;
import com.todo.auth.model.dto.AuthResult;
import com.todo.security.jwt.JwtService;
import com.todo.security.token.model.entity.RefreshToken;
import com.todo.security.token.service.RefreshTokenService;
import com.todo.user.model.enitity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthResponseBuilder {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthResult buildFullAuthResponse(User user, String message) {
        ResponseCookie accessTokenCookie = jwtService.generateAccessTokenCookie(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        ResponseCookie refreshTokenCookie = jwtService.generateRefreshTokenCookie(refreshToken.getToken());

        AuthResponse response = AuthResponse.builder()
                .message(message)
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().getAuthority()) // ERole -> String
                        .collect(Collectors.toSet()))
                .build();

        return AuthResult.builder()
                .response(response)
                .accessTokenCookie(accessTokenCookie)
                .refreshTokenCookie(refreshTokenCookie)
                .build();
    }

    public AuthResult buildAccessTokenOnlyResponse(User user, String message) {
        ResponseCookie accessTokenCookie = jwtService.generateAccessTokenCookie(user);

        AuthResponse response = AuthResponse.builder()
                .message(message)
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName().getAuthority())
                        .collect(Collectors.toSet()))
                .build();

        return AuthResult.builder()
                .response(response)
                .accessTokenCookie(accessTokenCookie)
                .refreshTokenCookie(null)
                .build();
    }
}
