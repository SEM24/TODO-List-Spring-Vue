package com.todo.user.service;

import com.todo.exception.GlobalServiceException;
import com.todo.security.userdetails.UserDetailsImpl;
import com.todo.user.model.enitity.ERole;
import com.todo.user.model.enitity.Role;
import com.todo.user.model.enitity.User;
import com.todo.user.repository.RoleRepository;
import com.todo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new GlobalServiceException(HttpStatus.NOT_FOUND));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Role getDefaultRole() {
        return roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Default role is not found."));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getUserFromUserDetails(UserDetailsImpl userDetails) {
        return findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new GlobalServiceException(HttpStatus.NOT_FOUND, "User not found with email: " + userDetails.getEmail()));
    }
    public User getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return getUserFromUserDetails(userDetails);
    }
}