package com.teamsync.api.features.taskcomment.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.taskcomment.dto.request.CreateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.request.UpdateCommentRequest;
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

  @PatchMapping("/{commentId}")
  public ApiResponse<CommentResponse> updateComment(
          @PathVariable String organizationId,
          @PathVariable String projectId,
          @PathVariable String columnId,
          @PathVariable String taskId,
          @PathVariable String commentId,
          @Valid @RequestBody UpdateCommentRequest request,
          @AuthenticationPrincipal CustomUserDetails currentUser
  ) {

      CommentResponse response =
              taskCommentService.updateComment(
                      organizationId,
                      projectId,
                      columnId,
                      taskId,
                      commentId,
                      currentUser.getUserId(),
                      request
              );

      return ApiResponse.<CommentResponse>builder()
              .success(true)
              .message("Comment updated successfully.")
              .data(response)
              .build();

  }

  @DeleteMapping("/{commentId}")
  public ApiResponse<Void> deleteComment(
          @PathVariable String organizationId,
          @PathVariable String projectId,
          @PathVariable String columnId,
          @PathVariable String taskId,
          @PathVariable String commentId,
          @AuthenticationPrincipal CustomUserDetails currentUser
  ) {

      taskCommentService.deleteComment(
              organizationId,
              projectId,
              columnId,
              taskId,
              commentId,
              currentUser.getUserId()
      );

      return ApiResponse.<Void>builder()
              .success(true)
              .message("Comment deleted successfully.")
              .build();

  }

}
