package com.teamsync.api.features.activity.controller;

import com.teamsync.api.common.pagination.PageQuery;
import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.activity.dto.response.ActivityResponse;
import com.teamsync.api.features.activity.service.ActivityService;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Activities", description = "Activity history")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations/{organizationId}")
public class ActivityController {

        private final ActivityService activityService;

        @Operation(summary = "Get organization activities", description = "Retrieves activities for the organization.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organization activities retrieved successfully."),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organization not found."),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
        })
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

        @Operation(summary = "Get project activities", description = "Retrieves activities for the project.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Project activities retrieved successfully."),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Project not found."),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
        })
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

        @Operation(summary = "Get task activities", description = "Retrieves activities for the task.")
        @ApiResponses({
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Task activities retrieved successfully."),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found."),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
        })
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
