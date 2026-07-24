package com.teamsync.api.features.task.dto.request;

import com.teamsync.api.features.task.entity.TaskPriority;
import com.teamsync.api.features.task.entity.TaskType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateTaskRequest(

        @NotBlank(message = "Title is required.")
        @Size(max = 150)
        String title,

        @Size(max = 2000)
        String description,

        @NotNull(message = "Priority is required.")
        TaskPriority priority,

        @NotNull(message = "Task type is required.")
        TaskType type,

        String assigneeId,

        Instant dueDate

) {}
