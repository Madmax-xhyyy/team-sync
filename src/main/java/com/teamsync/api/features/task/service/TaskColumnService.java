package com.teamsync.api.features.task.service;

import com.teamsync.api.features.task.dto.response.TaskColumnResponse;

import java.util.List;

public interface TaskColumnService {

  void createDefaultColumns(String projectId);

  List<TaskColumnResponse> getColumns(
      String organizationId,
      String projectId,
      String userId);

}
