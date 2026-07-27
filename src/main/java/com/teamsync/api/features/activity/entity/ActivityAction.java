package com.teamsync.api.features.activity.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the action performed.")
public enum ActivityAction {

  @Schema(description = "The entity was created.")
  CREATED,

  @Schema(description = "The entity was updated.")
  UPDATED,

  @Schema(description = "The entity was deleted.")
  DELETED,

  @Schema(description = "The entity was moved.")
  MOVED,

  @Schema(description = "The entity was assigned.")
  ASSIGNED,

  @Schema(description = "The entity was unassigned.")
  UNASSIGNED

}
