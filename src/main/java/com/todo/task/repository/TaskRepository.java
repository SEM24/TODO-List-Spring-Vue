package com.todo.task.repository;

import com.todo.task.model.enitity.Priority;
import com.todo.task.model.enitity.Task;
import com.todo.user.model.enitity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Task> findByUserIdAndCompletedOrderByCreatedAt(Long userId, Boolean completed);

    List<Task> findByUserIdAndPriority(Long userId, Priority priority);

    boolean existsByIdAndUserId(Long taskId, Long userId);

    //find by title
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId " +
            "AND LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm)) ORDER BY t.createdAt DESC")
    List<Task> findByUserIdAndTitleContainingIgnoreCase(Long userId, String searchTerm);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.completed = false " +
            "AND t.dueDate < :now order by t.dueDate ASC")
    List<Task> findOverdueTasksByUserId(Long userId, LocalDateTime now);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId " +
            "AND DATE(t.dueDate) = DATE(:date) ORDER BY t.dueDate ASC")
    List<Task> findTasksByUserIdAndDueDate(@Param("userId") Long userId,
                                           @Param("date") LocalDateTime date);

}
