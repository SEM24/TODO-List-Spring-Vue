package com.todo.task.service;

import com.todo.task.model.dto.TaskResponse;
import com.todo.task.model.enitity.Priority;

import java.util.List;

public interface TaskService {
    List<TaskResponse> getAllTasksForUser(Long userId);

    List<TaskResponse> getTaskByStatus(Long userId, boolean completed);

    List<TaskResponse> getTaskByPriority(Long userId, Priority priority);

    List<TaskResponse> searchTasks(Long userId, String searchTerm);

    List<TaskResponse> getOverdueTasks(Long userId);

    List<TaskResponse> getTodayTasks(Long userId);

    TaskResponse getTaskById(Long userId, Long taskId);

    void deleteTask(Long userId, Long taskId);
}
