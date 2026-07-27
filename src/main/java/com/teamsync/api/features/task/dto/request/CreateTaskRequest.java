package com.teamsync.api.features.task.dto.request;

import com.teamsync.api.features.task.entity.TaskPriority;
import com.teamsync.api.features.task.entity.TaskType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateTaskRequest(

    @Schema(description = "Task title", example = "Mobile App Development") @NotBlank(message = "Title is required.") @Size(max = 150) String title,

    @Schema(description = "Task description", example = "Developing a new mobile application") @Size(max = 2000) String description,

    @Schema(description = "Task priority", example = "Priority.HIGH") @NotNull(message = "Priority is required.") TaskPriority priority,

    @Schema(description = "Task type", example = "TaskType.BUG") @NotNull(message = "Task type is required.") TaskType type,

    @Schema(description = "User ID", example = "60d5fecb9c9c9d001a1c1c1c") String assigneeId,

    @Schema(description = "Due date", example = "2026-07-27T13:44:17.431Z") Instant dueDate

) {
}
