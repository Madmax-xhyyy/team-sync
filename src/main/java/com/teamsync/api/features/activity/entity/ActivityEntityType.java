package com.teamsync.api.features.activity.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the type of entity the activity is related to.")
public enum ActivityEntityType {

  @Schema(description = "The entity is an organization.")
  ORGANIZATION,

  @Schema(description = "The entity is a project.")
  PROJECT,

  @Schema(description = "The entity is a task column.")
  TASK_COLUMN,

  @Schema(description = "The entity is a task.")
  TASK,

  @Schema(description = "The entity is a comment.")
  COMMENT

}
