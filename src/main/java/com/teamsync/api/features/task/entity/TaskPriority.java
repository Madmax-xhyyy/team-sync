package com.teamsync.api.features.task.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the priority level of a task.")
public enum TaskPriority {

  @Schema(description = "The priority level is low.")
  LOW,

  @Schema(description = "The priority level is medium.")
  MEDIUM,

  @Schema(description = "The priority level is high.")
  HIGH,

  @Schema(description = "The priority level is critical.")
  CRITICAL

}
