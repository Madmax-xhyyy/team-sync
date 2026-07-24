package com.teamsync.api.features.task.service;

import com.teamsync.api.features.task.dto.request.AssignTaskRequest;
import com.teamsync.api.features.task.dto.request.CreateTaskRequest;
import com.teamsync.api.features.task.dto.request.MoveTaskRequest;
import com.teamsync.api.features.task.dto.request.UpdateTaskRequest;
import com.teamsync.api.features.task.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {

  TaskResponse createTask(
        String organizationId,
        String projectId,
        String columnId,
        String userId,
        CreateTaskRequest request
  );

  List<TaskResponse> getTasks(
        String organizationId,
        String projectId,
        String columnId,
        String userId
  );

  TaskResponse updateTask(
        String organizationId,
        String projectId,
        String columnId,
        String taskId,
        String currentUserId,
        UpdateTaskRequest request
  );

  void deleteTask(
        String organizationId,
        String projectId,
        String columnId,
        String taskId,
        String currentUserId
  );

  TaskResponse moveTask(
        String organizationId,
        String projectId,
        String sourceColumnId,
        String taskId,
        String currentUserId,
        MoveTaskRequest request
  );

  TaskResponse assignTask(
        String organizationId,
        String projectId,
        String columnId,
        String taskId,
        String currentUserId,
        AssignTaskRequest request
  );

  TaskResponse unassignTask(
        String organizationId,
        String projectId,
        String columnId,
        String taskId,
        String currentUserId
  );
}
