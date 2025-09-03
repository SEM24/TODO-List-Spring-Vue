package com.todo.user.repository;

import com.todo.user.model.enitity.ERole;
import com.todo.user.model.enitity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
