package com.teamsync.api.features.taskcomment.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.taskcomment.dto.request.CreateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.response.CommentResponse;
import com.teamsync.api.features.taskcomment.service.TaskCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations/{organizationId}/projects/{projectId}/columns/{columnId}/tasks/{taskId}/comments")
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    @PostMapping
    public ApiResponse<CommentResponse> createComment(
            @PathVariable String organizationId,
            @PathVariable String projectId,
            @PathVariable String columnId,
            @PathVariable String taskId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        CommentResponse response =
                taskCommentService.createComment(
                        organizationId,
                        projectId,
                        columnId,
                        taskId,
                        currentUser.getUserId(),
                        request
                );

        return ApiResponse.<CommentResponse>builder()
                .success(true)
                .message("Comment created successfully.")
                .data(response)
                .build();

    }

  @GetMapping
  public ApiResponse<List<CommentResponse>> getComments(
          @PathVariable String organizationId,
          @PathVariable String projectId,
          @PathVariable String columnId,
          @PathVariable String taskId,
          @AuthenticationPrincipal CustomUserDetails currentUser
  ) {

      List<CommentResponse> response =
              taskCommentService.getComments(
                      organizationId,
                      projectId,
                      columnId,
                      taskId,
                      currentUser.getUserId()
              );

      return ApiResponse.<List<CommentResponse>>builder()
              .success(true)
              .message("Comments retrieved successfully.")
              .data(response)
              .build();

  }

}
