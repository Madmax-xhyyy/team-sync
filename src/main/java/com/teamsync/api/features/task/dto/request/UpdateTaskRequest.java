package com.teamsync.api.features.task.dto.request;

import com.teamsync.api.features.task.entity.TaskPriority;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record UpdateTaskRequest(

  @Size(min = 1, max = 255)
  String title,

  String description,

  TaskPriority priority,

  Instant dueDate

) {}