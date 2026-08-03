package com.teamsync.api.common.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import com.teamsync.api.common.domain.task.TaskDomainService;
import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.task.entity.Task;
import com.teamsync.api.features.task.entity.TaskPriority;
import com.teamsync.api.features.task.entity.TaskType;
import com.teamsync.api.features.task.repository.TaskRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskDomainServiceTest {

    private static final String TASK_ID = "task-1";

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskDomainService service;

    private Task createTask() {

        Task task = Task.builder()
                .projectId("project-1")
                .columnId("column-1")
                .title("Fix login")
                .description("Fix login bug")
                .priority(TaskPriority.HIGH)
                .type(TaskType.BUG)
                .assigneeId("user-2")
                .reporterId("user-1")
                .dueDate(Instant.now())
                .position(1)
                .build();

        task.setId(TASK_ID);

        return task;
    }

    @Test
    void shouldReturnTaskWhenTaskExists() {

        // Arrange
        Task task = createTask();

        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.of(task));

        // Act
        Task result = service.getById(TASK_ID);

        // Assert
        assertNotNull(result);
        assertEquals(TASK_ID, result.getId());

        verify(taskRepository)
                .findById(TASK_ID);

    }

    @Test
    void shouldThrowWhenTaskDoesNotExist() {

        // Arrange
        when(taskRepository.findById(TASK_ID))
                .thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getById(TASK_ID));

        // Assert
        assertEquals(
                "Task not found.",
                exception.getMessage());

        verify(taskRepository)
                .findById(TASK_ID);

    }

}
