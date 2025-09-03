package com.todo.auth.service;

import com.todo.auth.model.dto.AuthResult;
import com.todo.auth.model.dto.LoginRequest;
import com.todo.auth.model.dto.LogoutResponse;
import com.todo.auth.model.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

public interface AuthService {
    @Transactional
    AuthResult register(RegisterRequest request);

    @Transactional
    AuthResult login(LoginRequest request);

    @Transactional
    AuthResult refreshAccessToken(HttpServletRequest request);

    @Transactional
    LogoutResponse logout(HttpServletRequest request);
}
