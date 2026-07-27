package com.teamsync.api.features.task.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.task.dto.response.TaskColumnResponse;
import com.teamsync.api.features.task.service.TaskColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@Tag(name = "Task Columns", description = "Task column management")
@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/projects/{projectId}/columns")
@RequiredArgsConstructor
public class TaskColumnController {

  private final TaskColumnService taskColumnService;

  @Operation(summary = "Get all columns", description = "Retrieves all columns for a project.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Columns retrieved successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
  })
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