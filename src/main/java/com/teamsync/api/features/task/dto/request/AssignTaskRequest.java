package com.teamsync.api.features.task.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AssignTaskRequest(

  @NotBlank
  String userId

) {}
