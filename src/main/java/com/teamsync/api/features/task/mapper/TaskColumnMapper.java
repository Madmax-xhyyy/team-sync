package com.teamsync.api.features.task.mapper;

import com.teamsync.api.features.task.dto.response.TaskColumnResponse;
import com.teamsync.api.features.task.entity.TaskColumn;
import org.springframework.stereotype.Component;

@Component
public class TaskColumnMapper {

  public TaskColumnResponse toResponse(TaskColumn column) {

    return new TaskColumnResponse(
        column.getId(),
        column.getProjectId(),
        column.getName(),
        column.getPosition());

  }

}
