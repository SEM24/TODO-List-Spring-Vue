package com.todo.task.controller;

import com.todo.task.model.dto.TaskCreateRequest;
import com.todo.task.model.dto.TaskResponse;
import com.todo.task.model.dto.TaskUpdateRequest;
import com.todo.task.service.TaskService;
import com.todo.user.model.enitity.User;
import com.todo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
@Slf4j
@Validated
@PreAuthorize("hasAuthority('ROLE_USER')")
@Tag(name = "Tasks", description = "CRUD operation for Task Controller")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "getTasksWithFilter")
    public ResponseEntity<List<TaskResponse>> getTasksWithFilter(Authentication authentication,
                                                                 @RequestParam(required = false) Boolean completed,
                                                                 @RequestParam(required = false) String priority,
                                                                 @RequestParam(required = false) String search,
                                                                 @RequestParam(required = false, defaultValue = "false") boolean overdue,
                                                                 @RequestParam(required = false, defaultValue = "false") boolean today) {
        User currentUser = userService.getCurrentUser(authentication);
        List<TaskResponse> tasks = taskService.getTasksWithFilter(
                currentUser.getId(), completed, priority, search, overdue, today
        );

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "getTask")
    public ResponseEntity<TaskResponse> getTask(Authentication authentication, @PathVariable Long taskId) {
        User user = userService.getCurrentUser(authentication);
        TaskResponse task = taskService.getTaskById(user.getId(), taskId);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    @Operation(summary = "createTask")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest taskCreateRequest, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        TaskResponse createdTask = taskService.createTask(user.getId(), taskCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{taskId}")
    @Operation(summary = "updateTask")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskUpdateRequest taskUpdateRequest, Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        TaskResponse updateTask = taskService.updateTask(user.getId(), taskId, taskUpdateRequest);
        return ResponseEntity.ok(updateTask);
    }

    @PatchMapping("/{taskId}/toggle")
    @Operation(summary = "toggleTaskCompletion")
    public ResponseEntity<TaskResponse> toggleTaskCompletion(
            @PathVariable Long taskId,
            Authentication authentication
    ) {
        User user = userService.getCurrentUser(authentication);
        TaskResponse updatedTask = taskService.toggleTaskCompletion(user.getId(), taskId);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "deleteTask")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long taskId,
            Authentication authentication
    ) {
        User user = userService.getCurrentUser(authentication);
        taskService.deleteTask(user.getId(), taskId);
        return ResponseEntity.noContent().build();
    }
}
