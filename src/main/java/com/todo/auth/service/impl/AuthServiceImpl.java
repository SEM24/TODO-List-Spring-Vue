package com.todo.auth.service.impl;


import com.todo.auth.model.dto.AuthResult;
import com.todo.auth.model.dto.LoginRequest;
import com.todo.auth.model.dto.LogoutResponse;
import com.todo.auth.model.dto.RegisterRequest;
import com.todo.auth.service.AuthService;
import com.todo.exception.GlobalServiceException;
import com.todo.security.jwt.JwtService;
import com.todo.security.token.model.entity.RefreshToken;
import com.todo.security.token.service.RefreshTokenService;
import com.todo.security.token.service.TokenValidationService;
import com.todo.security.userdetails.UserDetailsImpl;
import com.todo.user.model.enitity.AuthProviderType;
import com.todo.user.model.enitity.ERole;
import com.todo.user.model.enitity.Role;
import com.todo.user.model.enitity.User;
import com.todo.user.repository.RoleRepository;
import com.todo.user.repository.UserRepository;
import com.todo.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final TokenValidationService tokenValidationService;
    private final AuthResponseBuilder authResponseBuilder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final UserService userService;

    @Transactional
    @Override
    public AuthResult register(RegisterRequest request) {
        validateEmailNotExists(request.email());
        validateNotOAuthUser(request.email());
        User user = createNewUser(request);
        User savedUser = userRepository.save(user);

        log.info("New user registered: {}", savedUser.getEmail());
        return authResponseBuilder.buildFullAuthResponse(savedUser, "Registration successful");
    }

    @Transactional
    @Override
    public AuthResult login(LoginRequest request) {
        Authentication authentication = authenticateUser(request);
        User user = getUserFromAuthentication(authentication);

        log.info("User logged in: {}", user.getEmail());

        // Delete old refresh tokens for this user
        refreshTokenService.deleteByUser(user);

        return authResponseBuilder.buildFullAuthResponse(user, "Authentication successful");
    }

    @Transactional
    @Override
    public AuthResult refreshAccessToken(HttpServletRequest request) {
        String refreshTokenValue = tokenValidationService.extractRefreshTokenFromRequest(request);
        RefreshToken refreshToken = refreshTokenService.validateAndRefresh(refreshTokenValue);

        User user = refreshToken.getUser();
        log.info("Token refreshed for user: {}", user.getEmail());

        return authResponseBuilder.buildAccessTokenOnlyResponse(user, "Token refreshed successfully");
    }

    @Transactional
    @Override
    public LogoutResponse logout(HttpServletRequest request) {
        String refreshTokenValue = jwtService.extractRefreshToken(request);

        if (refreshTokenValue != null) {
            refreshTokenService.findByToken(refreshTokenValue)
                    .ifPresent(refreshToken -> {
                        log.info("User logged out: {}", refreshToken.getUser().getEmail());
                        refreshTokenService.delete(refreshToken);
                    });
        }

        return createLogoutResponse();
    }

    private void validateNotOAuthUser(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (user.getProvider() == AuthProviderType.GOOGLE) {
                throw new GlobalServiceException(HttpStatus.BAD_REQUEST,
                        "User already registered with Google. Please sign in with Google.");
            }
        });
    }

    private void validateEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new GlobalServiceException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }
    }

    private User createNewUser(RegisterRequest request) {
        //Role hardcoded since it's registration
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new GlobalServiceException(HttpStatus.NOT_FOUND, "Role not found: "));
        return User.builder()
                .username(request.email())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .provider(AuthProviderType.LOCAL)
                .roles(Set.of(userRole))
                .enabled(true)
                .emailVerified(false)
                .build();
    }

    private User createOAuthUser(String email, String name, String providerId) {
        Role userRole = userService.getDefaultRole();

        return User.builder()
                .username(email)
                .email(email)
                .name(name)
                .password("")
                .provider(AuthProviderType.GOOGLE)
                .providerId(providerId)
                .enabled(true)
                .emailVerified(true)
                .roles(Set.of(userRole))
                .build();
    }

    private Authentication authenticateUser(LoginRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
    }

    private User getUserFromAuthentication(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private LogoutResponse createLogoutResponse() {
        ResponseCookie accessTokenCookie = jwtService.clearAccessTokenCookie();
        ResponseCookie refreshTokenCookie = jwtService.clearRefreshTokenCookie();

        return LogoutResponse.builder()
                .message("Logged out successfully")
                .accessTokenCookie(accessTokenCookie)
                .refreshTokenCookie(refreshTokenCookie)
                .build();
    }

}
