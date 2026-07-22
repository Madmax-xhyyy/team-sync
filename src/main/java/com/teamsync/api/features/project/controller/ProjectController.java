package com.teamsync.api.features.project.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.project.dto.request.CreateProjectRequest;
import com.teamsync.api.features.project.dto.response.ProjectResponse;
import com.teamsync.api.features.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/projects")
@RequiredArgsConstructor
public class ProjectController {

  private final ProjectService projectService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<ProjectResponse> createProject(
      @PathVariable String organizationId,
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody CreateProjectRequest request) {

    ProjectResponse response = projectService.createProject(
        organizationId,
        currentUser.getUserId(),
        request);

    return ApiResponse.<ProjectResponse>builder()
        .success(true)
        .message("Project created successfully.")
        .data(response)
        .build();
  }

  @GetMapping
  public ApiResponse<List<ProjectResponse>> getProjects(
      @PathVariable String organizationId,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    List<ProjectResponse> response = projectService.getProjects(
        organizationId,
        currentUser.getUserId());

    return ApiResponse.<List<ProjectResponse>>builder()
        .success(true)
        .message("Projects retrieved successfully.")
        .data(response)
        .build();
  }
}