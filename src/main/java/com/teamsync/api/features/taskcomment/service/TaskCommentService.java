package com.teamsync.api.features.taskcomment.service;

import java.util.List;

import com.teamsync.api.features.taskcomment.dto.request.CreateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.request.UpdateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.response.CommentResponse;

public interface TaskCommentService {

  CommentResponse createComment(
        String organizationId,
        String projectId,
        String columnId,
        String taskId,
        String currentUserId,
        CreateCommentRequest request
  );

  List<CommentResponse> getComments(
        String organizationId,
        String projectId,
        String columnId,
        String taskId,
        String currentUserId
  );

  CommentResponse updateComment(
        String organizationId,
        String projectId,
        String columnId,
        String taskId,
        String commentId,
        String currentUserId,
        UpdateCommentRequest request
  );

  void deleteComment(
        String organizationId,
        String projectId,
        String columnId,
        String taskId,
        String commentId,
        String currentUserId
  );

}
