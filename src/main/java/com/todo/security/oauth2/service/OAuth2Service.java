package com.todo.security.oauth2.service;

import com.todo.auth.model.dto.AuthResult;
import org.springframework.transaction.annotation.Transactional;

public interface OAuth2Service {
    @Transactional
    AuthResult processOAuthLogin(String email, String name, String providerId);
}
