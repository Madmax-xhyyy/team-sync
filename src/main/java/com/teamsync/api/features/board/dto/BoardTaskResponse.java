package com.teamsync.api.features.board.dto;

import com.teamsync.api.features.task.entity.TaskPriority;
import com.teamsync.api.features.task.entity.TaskType;

public record BoardTaskResponse(

        String id,

        String title,

        TaskPriority priority,

        TaskType type,

        String assigneeId,

        Integer position

) {}
