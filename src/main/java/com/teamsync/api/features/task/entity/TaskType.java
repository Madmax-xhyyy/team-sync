package com.teamsync.api.features.task.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the type of a task.")
public enum TaskType {

    @Schema(description = "The task is a general task.")
    TASK,

    @Schema(description = "The task is a bug.")
    BUG,

    @Schema(description = "The task is a story.")
    STORY,

    @Schema(description = "The task is an epic.")
    EPIC
}
