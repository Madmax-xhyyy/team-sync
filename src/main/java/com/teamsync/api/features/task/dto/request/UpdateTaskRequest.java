package com.teamsync.api.features.task.dto.request;

import com.teamsync.api.features.task.entity.TaskPriority;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record UpdateTaskRequest(

    @Schema(description = "Task title", example = "Mobile App Development") @Size(min = 1, max = 255) String title,

    @Schema(description = "Task description", example = "Developing a new mobile application") String description,

    @Schema(description = "Task priority", example = "Priority.HIGH") TaskPriority priority,

    @Schema(description = "Task due date", example = "2026-07-27T13:44:17.431Z") Instant dueDate

) {
}