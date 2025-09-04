package com.todo.task.service;

import com.todo.task.model.dto.TaskCreateRequest;
import com.todo.task.model.dto.TaskResponse;
import com.todo.task.model.dto.TaskUpdateRequest;
import com.todo.task.model.enitity.Priority;

import java.util.List;

public interface TaskService {
    List<TaskResponse> getAllTasksForUser(Long userId);

    List<TaskResponse> getTaskByStatus(Long userId, boolean completed);

    List<TaskResponse> getTaskByPriority(Long userId, Priority priority);

    List<TaskResponse> searchTasks(Long userId, String searchTerm);

    List<TaskResponse> getOverdueTasks(Long userId);

    List<TaskResponse> getTodayTasks(Long userId);

    TaskResponse createTask(Long userId, TaskCreateRequest request);

    TaskResponse updateTask(Long userId, Long taskId, TaskUpdateRequest request);

    TaskResponse toggleTaskCompletion(Long userId, Long taskId);

    TaskResponse getTaskById(Long userId, Long taskId);

    void deleteTask(Long userId, Long taskId);

    List<TaskResponse> getTasksWithFilter(Long id, Boolean completed, String priority, String search, boolean overdue, boolean today);
}
