package com.teamsync.api.features.task.service;

import com.teamsync.api.common.security.TaskColumnAuthorizationService;
import com.teamsync.api.features.task.dto.request.CreateTaskRequest;
import com.teamsync.api.features.task.dto.response.TaskResponse;
import com.teamsync.api.features.task.entity.Task;
import com.teamsync.api.features.task.mapper.TaskMapper;
import com.teamsync.api.features.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskColumnAuthorizationService taskColumnAuthorizationService;

    @Override
    public TaskResponse createTask(
            String organizationId,
            String projectId,
            String columnId,
            String userId,
            CreateTaskRequest request
    ) {

        taskColumnAuthorizationService.requireTaskColumnAccess(
                organizationId,
                projectId,
                columnId,
                userId
        );

        Integer position =
                (int) taskRepository.countByColumnId(columnId) + 1;

        Task task = taskMapper.toEntity(
                request,
                projectId,
                columnId,
                userId,
                position
        );

        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);

    }

    @Override
    public List<TaskResponse> getTasks(
            String organizationId,
            String projectId,
            String columnId,
            String userId
    ) {

        taskColumnAuthorizationService.requireTaskColumnAccess(
                organizationId,
                projectId,
                columnId,
                userId
        );

        return taskRepository
                .findByColumnIdOrderByPositionAsc(columnId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();

    }

}
