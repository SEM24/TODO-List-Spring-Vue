package com.todo.task.service.impl;

import com.todo.exception.GlobalServiceException;
import com.todo.task.model.dto.TaskCreateRequest;
import com.todo.task.model.dto.TaskResponse;
import com.todo.task.model.dto.TaskUpdateRequest;
import com.todo.task.model.enitity.Priority;
import com.todo.task.model.enitity.Task;
import com.todo.task.repository.TaskRepository;
import com.todo.task.service.TaskService;
import com.todo.user.model.enitity.User;
import com.todo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    public List<TaskResponse> getAllTasksForUser(Long userId) {
        return taskRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(TaskResponse::from).toList();
    }

    @Override
    public List<TaskResponse> getTaskByStatus(Long userId, boolean completed) {
        return taskRepository.findByUserIdAndCompletedOrderByCreatedAt(
                userId, completed).stream().map(TaskResponse::from).toList();
    }

    @Override
    public List<TaskResponse> getTaskByPriority(Long userId, Priority priority) {
        return taskRepository.findByUserIdAndPriority(userId, priority)
                .stream().map(TaskResponse::from).toList();
    }

    @Override
    public List<TaskResponse> searchTasks(Long userId, String searchTerm) {
        return taskRepository.findByUserIdAndTitleContainingIgnoreCase(userId, searchTerm).stream()
                .map(TaskResponse::from).toList();
    }

    @Override
    public List<TaskResponse> getOverdueTasks(Long userId) {
        return taskRepository.findOverdueTasksByUserId(userId, LocalDateTime.now()).stream()
                .map(TaskResponse::from).toList();
    }

    @Override
    public List<TaskResponse> getTodayTasks(Long userId) {
        return taskRepository.findTasksByUserIdAndDueDate(userId, LocalDateTime.now())
                .stream()
                .map(TaskResponse::from)
                .toList();
    }

    public TaskResponse createTask(Long userId, TaskCreateRequest request) {
        User user = userService.findById(userId);

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .dueDate(request.dueDate())
                .completed(false)
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);
        return TaskResponse.from(savedTask);
    }

    public TaskResponse updateTask(Long userId, Long taskId, TaskUpdateRequest request) {
        Task task = getTaskByIdAndUserId(taskId, userId);

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());

        if (request.completed() != null) {
            task.setCompleted(request.completed());
        }

        Task savedTask = taskRepository.save(task);
        return TaskResponse.from(savedTask);
    }

    public TaskResponse toggleTaskCompletion(Long userId, Long taskId) {
        Task task = getTaskByIdAndUserId(taskId, userId);
        task.setCompleted(!task.getCompleted());
        Task savedTask = taskRepository.save(task);
        return TaskResponse.from(savedTask);
    }

    @Override
    public TaskResponse getTaskById(Long userId, Long taskId) {
        Task task = getTaskByIdAndUserId(userId, taskId);
        return TaskResponse.from(task);
    }

    @Override
    public void deleteTask(Long userId, Long taskId) {
        Task task = getTaskByIdAndUserId(userId, taskId);
        taskRepository.delete(task);
    }

    private Task getTaskByIdAndUserId(Long userId, Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(()
                -> new GlobalServiceException(HttpStatus.NOT_FOUND, "Task not found with id " + taskId));
        if (!task.getUser().getId().equals(userId)) {
            throw new GlobalServiceException(HttpStatus.FORBIDDEN);
        }
        return task;
    }
}
