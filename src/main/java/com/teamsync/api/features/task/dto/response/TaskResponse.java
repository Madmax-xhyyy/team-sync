package com.teamsync.api.features.task.dto.response;

import com.teamsync.api.features.task.entity.TaskPriority;

import java.time.Instant;

public record TaskResponse(

        String id,

        String projectId,

        String columnId,

        String title,

        String description,

        TaskPriority priority,

        String assigneeId,

        String reporterId,

        Instant dueDate,

        Integer position,

        Instant createdAt,

        Instant updatedAt

) {}