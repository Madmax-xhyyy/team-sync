package com.teamsync.api.features.task.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.task.dto.response.TaskColumnResponse;
import com.teamsync.api.features.task.service.TaskColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/projects/{projectId}/columns")
@RequiredArgsConstructor
public class TaskColumnController {

  private final TaskColumnService taskColumnService;

  @GetMapping
  public ApiResponse<List<TaskColumnResponse>> getColumns(
      @PathVariable String organizationId,
      @PathVariable String projectId,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    List<TaskColumnResponse> response = taskColumnService.getColumns(
        organizationId,
        projectId,
        currentUser.getUserId());

    return ApiResponse.<List<TaskColumnResponse>>builder()
        .success(true)
        .message("Columns retrieved successfully.")
        .data(response)
        .build();

  }

}