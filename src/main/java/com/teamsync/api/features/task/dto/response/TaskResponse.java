package com.teamsync.api.features.task.dto.response;

import com.teamsync.api.features.task.entity.TaskPriority;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record TaskResponse(

    @Schema(description = "Task ID", example = "60d5fecb9c9c9d001a1c1c1c") String id,

    @Schema(description = "Project ID", example = "60d5fecb9c9c9d001a1c1c1c") String projectId,

    @Schema(description = "Column ID", example = "60d5fecb9c9c9d001a1c1c1c") String columnId,

    @Schema(description = "Task title", example = "Mobile App Development") String title,

    @Schema(description = "Task description", example = "Developing a new mobile application") String description,

    @Schema(description = "Task priority", example = "Priority.HIGH") TaskPriority priority,

    @Schema(description = "Assignee ID", example = "60d5fecb9c9c9d001a1c1c1c") String assigneeId,

    @Schema(description = "Reporter ID", example = "60d5fecb9c9c9d001a1c1c1c") String reporterId,

    @Schema(description = "Due date", example = "2026-07-27T13:44:17.431Z") Instant dueDate,

    @Schema(description = "Task position", example = "0") Integer position,

    @Schema(description = "Task creation date", example = "2026-07-27T13:44:17.431Z") Instant createdAt,

    @Schema(description = "Task update date", example = "2026-07-27T13:44:17.431Z") Instant updatedAt

) {
}