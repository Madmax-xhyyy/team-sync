package com.teamsync.api.features.activity.controller;

import com.teamsync.api.common.pagination.PageQuery;
import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.activity.dto.response.ActivityResponse;
import com.teamsync.api.features.activity.service.ActivityService;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations/{organizationId}")
public class ActivityController {

  private final ActivityService activityService;

  @GetMapping("/activities")
  public ApiResponse<List<ActivityResponse>> getOrganizationActivities(
      @PathVariable String organizationId,
      @ModelAttribute PageQuery pageQuery,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    List<ActivityResponse> response = activityService.getOrganizationActivities(
        organizationId,
        currentUser.getUserId());

    return ApiResponse.<List<ActivityResponse>>builder()
        .success(true)
        .message("Organization activities retrieved successfully.")
        .data(response)
        .build();

  }

  @GetMapping("/projects/{projectId}/activities")
  public ApiResponse<List<ActivityResponse>> getProjectActivities(
      @PathVariable String organizationId,
      @PathVariable String projectId,
      @ModelAttribute PageQuery pageQuery,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    List<ActivityResponse> response = activityService.getProjectActivities(
        organizationId,
        projectId,
        currentUser.getUserId());

    return ApiResponse.<List<ActivityResponse>>builder()
        .success(true)
        .message("Project activities retrieved successfully.")
        .data(response)
        .build();

  }

  @GetMapping("/projects/{projectId}/columns/{columnId}/tasks/{taskId}/activities")
  public ApiResponse<List<ActivityResponse>> getTaskActivities(
      @PathVariable String organizationId,
      @PathVariable String projectId,
      @PathVariable String columnId,
      @PathVariable String taskId,
      @ModelAttribute PageQuery pageQuery,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    List<ActivityResponse> response = activityService.getTaskActivities(
        organizationId,
        projectId,
        columnId,
        taskId,
        currentUser.getUserId());

    return ApiResponse.<List<ActivityResponse>>builder()
        .success(true)
        .message("Task activities retrieved successfully.")
        .data(response)
        .build();

  }

}
