package com.teamsync.api.features.board.dto;

import com.teamsync.api.features.task.entity.TaskPriority;
import com.teamsync.api.features.task.entity.TaskType;

import io.swagger.v3.oas.annotations.media.Schema;

public record BoardTaskResponse(

    @Schema(description = "Task ID", example = "1") String id,

    @Schema(description = "Task title", example = "Task 1") String title,

    @Schema(description = "Task priority", example = "HIGH") TaskPriority priority,

    @Schema(description = "Task type", example = "BUG") TaskType type,

    @Schema(description = "Assignee ID", example = "1") String assigneeId,

    @Schema(description = "Task position", example = "0") Integer position

) {
}
