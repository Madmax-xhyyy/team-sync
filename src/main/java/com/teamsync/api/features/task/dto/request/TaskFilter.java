package com.teamsync.api.features.task.dto.request;

import com.teamsync.api.features.task.entity.TaskPriority;
// import com.teamsync.api.features.task.entity.TaskStatus;
import com.teamsync.api.features.task.entity.TaskType;

public record TaskFilter(

    // TaskStatus status,

    TaskPriority priority,

    TaskType type,

    String assigneeId

) {
}
