package com.teamsync.api.features.taskcomment.controller;

import com.teamsync.api.common.pagination.PageQuery;
import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.taskcomment.dto.request.CreateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.request.UpdateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.response.CommentResponse;
import com.teamsync.api.features.taskcomment.service.TaskCommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Task Comments", description = "Manages comments on tasks")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations/{organizationId}/projects/{projectId}/columns/{columnId}/tasks/{taskId}/comments")
public class TaskCommentController {

  private final TaskCommentService taskCommentService;

  @Operation(summary = "Create comment", description = "Creates a new comment.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment created successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
  })
  @PostMapping
  public ApiResponse<CommentResponse> createComment(
      @PathVariable String organizationId,
      @PathVariable String projectId,
      @PathVariable String columnId,
      @PathVariable String taskId,
      @Valid @RequestBody CreateCommentRequest request,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    CommentResponse response = taskCommentService.createComment(
        organizationId,
        projectId,
        columnId,
        taskId,
        currentUser.getUserId(),
        request);

    return ApiResponse.<CommentResponse>builder()
        .success(true)
        .message("Comment created successfully.")
        .data(response)
        .build();

  }

  @Operation(summary = "Get comments", description = "Retrieves all comments for a task.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comments retrieved successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task not found."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
  })
  @GetMapping
  public ApiResponse<List<CommentResponse>> getComments(
      @PathVariable String organizationId,
      @PathVariable String projectId,
      @PathVariable String columnId,
      @PathVariable String taskId,
      @ModelAttribute PageQuery pageQuery,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    List<CommentResponse> response = taskCommentService.getComments(
        organizationId,
        projectId,
        columnId,
        taskId,
        currentUser.getUserId());

    return ApiResponse.<List<CommentResponse>>builder()
        .success(true)
        .message("Comments retrieved successfully.")
        .data(response)
        .build();

  }

  @Operation(summary = "Update comment", description = "Updates an existing comment.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment updated successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task or comment not found."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
  })
  @PatchMapping("/{commentId}")
  public ApiResponse<CommentResponse> updateComment(
      @PathVariable String organizationId,
      @PathVariable String projectId,
      @PathVariable String columnId,
      @PathVariable String taskId,
      @PathVariable String commentId,
      @Valid @RequestBody UpdateCommentRequest request,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    CommentResponse response = taskCommentService.updateComment(
        organizationId,
        projectId,
        columnId,
        taskId,
        commentId,
        currentUser.getUserId(),
        request);

    return ApiResponse.<CommentResponse>builder()
        .success(true)
        .message("Comment updated successfully.")
        .data(response)
        .build();

  }

  @Operation(summary = "Delete comment", description = "Deletes a comment.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Comment deleted successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Task or comment not found."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
  })
  @DeleteMapping("/{commentId}")
  public ApiResponse<Void> deleteComment(
      @PathVariable String organizationId,
      @PathVariable String projectId,
      @PathVariable String columnId,
      @PathVariable String taskId,
      @PathVariable String commentId,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    taskCommentService.deleteComment(
        organizationId,
        projectId,
        columnId,
        taskId,
        commentId,
        currentUser.getUserId());

    return ApiResponse.<Void>builder()
        .success(true)
        .message("Comment deleted successfully.")
        .build();

  }

}
