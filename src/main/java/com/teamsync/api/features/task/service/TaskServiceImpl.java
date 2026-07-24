package com.teamsync.api.features.task.service;

import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.common.security.PermissionService;
import com.teamsync.api.common.security.TaskAuthorizationService;
import com.teamsync.api.common.security.TaskColumnAuthorizationService;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.task.dto.request.CreateTaskRequest;
import com.teamsync.api.features.task.dto.request.UpdateTaskRequest;
import com.teamsync.api.features.task.dto.response.TaskResponse;
import com.teamsync.api.features.task.entity.Task;
import com.teamsync.api.features.task.mapper.TaskMapper;
import com.teamsync.api.features.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final TaskColumnAuthorizationService taskColumnAuthorizationService;
  private final OrganizationAuthorizationService organizationAuthorizationService;
  private final TaskAuthorizationService taskAuthorizationService;
  private final PermissionService permissionService;

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

  @Override
  @Transactional
  public TaskResponse updateTask(
          String organizationId,
          String projectId,
          String columnId,
          String taskId,
          String currentUserId,
          UpdateTaskRequest request
  ) {

  // Verify organization membership
  OrganizationMember currentMember =
          organizationAuthorizationService.requireOrganizationAccess(
                  organizationId,
                  currentUserId
          );

  // Verify permission
  permissionService.requireTaskUpdatePermission(currentMember);

  // Verify task belongs to the project and organization
  Task task =
          taskAuthorizationService.requireTaskAccess(
                  organizationId,
                  projectId,
                  columnId,
                  taskId,
                  currentUserId
          );

  // Partial update
  if (request.title() != null) {
          task.setTitle(request.title());
  }

  if (request.description() != null) {
          task.setDescription(request.description());
  }

  if (request.priority() != null) {
          task.setPriority(request.priority());
  }

  if (request.dueDate() != null) {
          task.setDueDate(request.dueDate());
  }

  Task savedTask = taskRepository.save(task);

  return taskMapper.toResponse(savedTask);
  }

  @Override
  @Transactional
  public void deleteTask(
        String organizationId,
        String projectId,
        String columnId,
        String taskId,
        String currentUserId
  ) {

    // Verify organization membership
    OrganizationMember currentMember =
            organizationAuthorizationService
                    .requireOrganizationAccess(
                            organizationId,
                            currentUserId
                    );

    // Verify permission
    permissionService.requireTaskDeletePermission(currentMember);

    // Verify task access
    Task task =
            taskAuthorizationService.requireTaskAccess(
                    organizationId,
                    projectId,
                    columnId,
                    taskId,
                    currentUserId
            );

    taskRepository.delete(task);

  }

}
