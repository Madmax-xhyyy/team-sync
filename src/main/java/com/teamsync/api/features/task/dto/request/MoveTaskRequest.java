package com.teamsync.api.features.task.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MoveTaskRequest(

  @NotBlank
  String targetColumnId,

  @NotNull
  @Min(0)
  Integer position

) {}
