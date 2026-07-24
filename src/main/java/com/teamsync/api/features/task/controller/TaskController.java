package com.teamsync.api.features.task.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.task.dto.request.CreateTaskRequest;
import com.teamsync.api.features.task.dto.request.UpdateTaskRequest;
import com.teamsync.api.features.task.dto.response.TaskResponse;
import com.teamsync.api.features.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/projects/{projectId}/columns/{columnId}/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<TaskResponse> createTask(
          @PathVariable String organizationId,
          @PathVariable String projectId,
          @PathVariable String columnId,
          @AuthenticationPrincipal CustomUserDetails currentUser,
          @Valid @RequestBody CreateTaskRequest request
  ) {

      TaskResponse response = taskService.createTask(
              organizationId,
              projectId,
              columnId,
              currentUser.getUserId(),
              request
      );

      return ApiResponse.<TaskResponse>builder()
              .success(true)
              .message("Task created successfully.")
              .data(response)
              .build();
  }

  @GetMapping
  public ApiResponse<List<TaskResponse>> getTasks(
          @PathVariable String organizationId,
          @PathVariable String projectId,
          @PathVariable String columnId,
          @AuthenticationPrincipal CustomUserDetails currentUser
  ) {

      List<TaskResponse> response = taskService.getTasks(
              organizationId,
              projectId,
              columnId,
              currentUser.getUserId()
      );

      return ApiResponse.<List<TaskResponse>>builder()
              .success(true)
              .message("Tasks retrieved successfully.")
              .data(response)
              .build();
  }

  @PatchMapping("/{taskId}")
  public ApiResponse<TaskResponse> updateTask(
        @PathVariable String organizationId,
        @PathVariable String projectId,
        @PathVariable String columnId,
        @PathVariable String taskId,
        @Valid @RequestBody UpdateTaskRequest request,
        @AuthenticationPrincipal CustomUserDetails currentUser
  ) {

      TaskResponse response =
            taskService.updateTask(
                  organizationId,
                  projectId,
                  columnId,
                  taskId,
                  currentUser.getUserId(),
                  request
            );

      return ApiResponse.<TaskResponse>builder()
            .success(true)
            .message("Task updated successfully.")
            .data(response)
            .build();
  }
  
  @DeleteMapping("/{taskId}")
  public ApiResponse<Void> deleteTask(
        @PathVariable String organizationId,
        @PathVariable String projectId,
        @PathVariable String columnId,
        @PathVariable String taskId,
        @AuthenticationPrincipal CustomUserDetails currentUser
  ) {

    taskService.deleteTask(
          organizationId,
          projectId,
          columnId,
          taskId,
          currentUser.getUserId()
    );

    return ApiResponse.<Void>builder()
          .success(true)
          .message("Task deleted successfully.")
          .build();
  }

}
