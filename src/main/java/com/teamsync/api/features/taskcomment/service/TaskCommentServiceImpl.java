package com.teamsync.api.features.taskcomment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.common.security.PermissionService;
import com.teamsync.api.common.security.TaskAuthorizationService;
import com.teamsync.api.common.security.TaskCommentAuthorizationService;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.taskcomment.dto.request.CreateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.request.UpdateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.response.CommentResponse;
import com.teamsync.api.features.taskcomment.entity.TaskComment;
import com.teamsync.api.features.taskcomment.mapper.TaskCommentMapper;
import com.teamsync.api.features.taskcomment.repository.TaskCommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskCommentServiceImpl implements TaskCommentService {

  private final OrganizationAuthorizationService organizationAuthorizationService;
  private final PermissionService permissionService;
  private final TaskAuthorizationService taskAuthorizationService;
  private final TaskCommentMapper taskCommentMapper;
  private final TaskCommentRepository taskCommentRepository;
  private final TaskCommentAuthorizationService taskCommentAuthorizationService;
  
  @Override
  @Transactional
  public CommentResponse createComment(
          String organizationId,
          String projectId,
          String columnId,
          String taskId,
          String currentUserId,
          CreateCommentRequest request
  ) {

      // Verify organization membership
      OrganizationMember currentMember =
              organizationAuthorizationService.requireOrganizationAccess(
                      organizationId,
                      currentUserId
              );

      // Verify permission
      permissionService.requireCommentCreatePermission(currentMember);

      // Verify task access
      taskAuthorizationService.requireTaskAccess(
              organizationId,
              projectId,
              columnId,
              taskId,
              currentUserId
      );

      // Create comment
      TaskComment comment =
              taskCommentMapper.toEntity(
                      request,
                      taskId,
                      currentUserId
              );

      TaskComment savedComment =
              taskCommentRepository.save(comment);

      return taskCommentMapper.toResponse(savedComment);

  }

  @Override
  @Transactional(readOnly = true)
  public List<CommentResponse> getComments(
          String organizationId,
          String projectId,
          String columnId,
          String taskId,
          String currentUserId
  ) {

      // Verify organization membership
      organizationAuthorizationService.requireOrganizationAccess(
              organizationId,
              currentUserId
      );

      // Verify task access
      taskAuthorizationService.requireTaskAccess(
              organizationId,
              projectId,
              columnId,
              taskId,
              currentUserId
      );

      return taskCommentRepository
              .findByTaskIdOrderByCreatedAtAsc(taskId)
              .stream()
              .map(taskCommentMapper::toResponse)
              .toList();

  }

  @Override
  @Transactional
  public CommentResponse updateComment(
          String organizationId,
          String projectId,
          String columnId,
          String taskId,
          String commentId,
          String currentUserId,
          UpdateCommentRequest request
  ) {

      // Verify organization membership
      OrganizationMember currentMember =
              organizationAuthorizationService.requireOrganizationAccess(
                      organizationId,
                      currentUserId
              );

      // Verify comment access
      TaskComment comment =
              taskCommentAuthorizationService.requireCommentAccess(
                      organizationId,
                      projectId,
                      columnId,
                      taskId,
                      commentId,
                      currentUserId
              );

      // Verify permission
      permissionService.requireCommentUpdatePermission(
              currentMember,
              comment.getUserId(),
              currentUserId
      );

      comment.setContent(request.content());
      comment.setEdited(true);

      TaskComment updatedComment =
              taskCommentRepository.save(comment);

      return taskCommentMapper.toResponse(updatedComment);
  }

  @Override
  @Transactional
  public void deleteComment(
          String organizationId,
          String projectId,
          String columnId,
          String taskId,
          String commentId,
          String currentUserId
  ) {
      // Verify organization membership
      OrganizationMember currentMember =
              organizationAuthorizationService.requireOrganizationAccess(
                      organizationId,
                      currentUserId
              );

      // Verify comment access
      TaskComment comment =
              taskCommentAuthorizationService.requireCommentAccess(
                      organizationId,
                      projectId,
                      columnId,
                      taskId,
                      commentId,
                      currentUserId
              );

      // Verify permission
      permissionService.requireCommentDeletePermission(
              currentMember,
              comment.getUserId(),
              currentUserId
      );
      taskCommentRepository.delete(comment);
  }
}
