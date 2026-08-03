package com.teamsync.api.features.task.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.teamsync.api.features.task.dto.request.CreateTaskRequest;
import com.teamsync.api.features.task.dto.response.TaskResponse;
import com.teamsync.api.features.task.entity.Task;
import com.teamsync.api.features.task.entity.TaskPriority;
import com.teamsync.api.features.task.entity.TaskType;

class TaskMapperTest {

    private static final String PROJECT_ID = "project-1";
    private static final String COLUMN_ID = "column-1";
    private static final String TASK_ID = "task-1";
    private static final String REPORTER_ID = "user-1";
    private static final String ASSIGNEE_ID = "user-2";

    private final TaskMapper mapper = new TaskMapper();

    @Test
    void shouldMapToEntity() {

        // Arrange
        Instant dueDate = Instant.now();

        CreateTaskRequest request = new CreateTaskRequest(
                "Implement Login",
                "Implement JWT authentication",
                TaskPriority.HIGH,
                TaskType.BUG,
                ASSIGNEE_ID,
                dueDate);

        // Act
        Task task = mapper.toEntity(
                request,
                PROJECT_ID,
                COLUMN_ID,
                REPORTER_ID,
                3);

        // Assert
        assertAll(
                () -> assertEquals(PROJECT_ID, task.getProjectId()),
                () -> assertEquals(COLUMN_ID, task.getColumnId()),
                () -> assertEquals("Implement Login", task.getTitle()),
                () -> assertEquals("Implement JWT authentication", task.getDescription()),
                () -> assertEquals(TaskPriority.HIGH, task.getPriority()),
                () -> assertEquals(TaskType.BUG, task.getType()),
                () -> assertEquals(ASSIGNEE_ID, task.getAssigneeId()),
                () -> assertEquals(REPORTER_ID, task.getReporterId()),
                () -> assertEquals(dueDate, task.getDueDate()),
                () -> assertEquals(3, task.getPosition())
        );

    }

    @Test
    void shouldMapToResponse() {

        // Arrange
        Instant dueDate = Instant.now();
        Instant createdAt = dueDate.plusSeconds(60);
        Instant updatedAt = createdAt.plusSeconds(60);

        Task task = Task.builder()
                .projectId(PROJECT_ID)
                .columnId(COLUMN_ID)
                .title("Implement Login")
                .description("Implement JWT authentication")
                .priority(TaskPriority.HIGH)
                .type(TaskType.BUG)
                .assigneeId(ASSIGNEE_ID)
                .reporterId(REPORTER_ID)
                .dueDate(dueDate)
                .position(3)
                .build();

        task.setId(TASK_ID);
        task.setCreatedAt(createdAt);
        task.setUpdatedAt(updatedAt);

        // Act
        TaskResponse response = mapper.toResponse(task);

        // Assert
        assertAll(
                () -> assertEquals(TASK_ID, response.id()),
                () -> assertEquals(PROJECT_ID, response.projectId()),
                () -> assertEquals(COLUMN_ID, response.columnId()),
                () -> assertEquals("Implement Login", response.title()),
                () -> assertEquals("Implement JWT authentication", response.description()),
                () -> assertEquals(TaskPriority.HIGH, response.priority()),
                () -> assertEquals(ASSIGNEE_ID, response.assigneeId()),
                () -> assertEquals(REPORTER_ID, response.reporterId()),
                () -> assertEquals(dueDate, response.dueDate()),
                () -> assertEquals(3, response.position()),
                () -> assertEquals(createdAt, response.createdAt()),
                () -> assertEquals(updatedAt, response.updatedAt())
        );

    }

}