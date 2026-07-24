package com.teamsync.api.features.task.service;

import com.teamsync.api.features.task.dto.request.CreateTaskRequest;
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

}
