package com.teamsync.api.features.project.controller;

import com.teamsync.api.common.pagination.PageResponse;
import com.teamsync.api.common.pagination.PageQuery;
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
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Projects", description = "Manage organization projects")
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
  public ApiResponse<PageResponse<ProjectResponse>> getProjects(
      @PathVariable String organizationId,
      @ModelAttribute PageQuery pageQuery,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    PageResponse<ProjectResponse> response = projectService.getProjects(
        organizationId,
        currentUser.getUserId(),
        pageQuery);

    return ApiResponse.<PageResponse<ProjectResponse>>builder()
        .success(true)
        .message("Projects retrieved successfully.")
        .data(response)
        .build();

  }
}