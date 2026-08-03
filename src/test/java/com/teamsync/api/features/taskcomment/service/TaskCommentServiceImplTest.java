package com.teamsync.api.features.taskcomment.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.common.security.PermissionService;
import com.teamsync.api.common.security.TaskAuthorizationService;
import com.teamsync.api.common.security.TaskCommentAuthorizationService;
import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;
import com.teamsync.api.features.activity.service.ActivityService;
import com.teamsync.api.features.organization.entity.OrganizationRole;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.taskcomment.dto.request.CreateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.request.UpdateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.response.CommentResponse;
import com.teamsync.api.features.taskcomment.entity.TaskComment;
import com.teamsync.api.features.taskcomment.mapper.TaskCommentMapper;
import com.teamsync.api.features.taskcomment.repository.TaskCommentRepository;

@ExtendWith(MockitoExtension.class)
class TaskCommentServiceImplTest {

    private static final String ORGANIZATION_ID = "organization-1";
    private static final String PROJECT_ID = "project-1";
    private static final String COLUMN_ID = "column-1";
    private static final String TASK_ID = "task-1";
    private static final String COMMENT_ID = "comment-1";
    private static final String USER_ID = "user-1";

    @Mock
    private OrganizationAuthorizationService organizationAuthorizationService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private TaskAuthorizationService taskAuthorizationService;

    @Mock
    private TaskCommentMapper taskCommentMapper;

    @Mock
    private TaskCommentRepository taskCommentRepository;

    @Mock
    private TaskCommentAuthorizationService taskCommentAuthorizationService;

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private TaskCommentServiceImpl service;

    private OrganizationMember createMember() {

    return OrganizationMember.builder()
            .organizationId(ORGANIZATION_ID)
            .userId(USER_ID)
            .role(OrganizationRole.MEMBER)
            .joinedAt(Instant.now())
            .build();

  }

  private TaskComment createComment() {

      TaskComment comment = TaskComment.builder()
              .taskId(TASK_ID)
              .userId(USER_ID)
              .content("First comment")
              .edited(false)
              .build();

      comment.setId(COMMENT_ID);

      return comment;

  }

  private CommentResponse createResponse() {

      return new CommentResponse(
              COMMENT_ID,
              TASK_ID,
              USER_ID,
              "First comment",
              false,
              Instant.now(),
              Instant.now()
      );

  }

  @Test
  void shouldCreateComment() {

      CreateCommentRequest request =
              new CreateCommentRequest("First comment");

      OrganizationMember member = createMember();

      TaskComment comment = createComment();

      CommentResponse response = createResponse();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(member);

      when(taskCommentMapper.toEntity(
              request,
              TASK_ID,
              USER_ID))
              .thenReturn(comment);

      when(taskCommentRepository.save(comment))
              .thenReturn(comment);

      when(taskCommentMapper.toResponse(comment))
              .thenReturn(response);

      CommentResponse result =
              service.createComment(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID,
                      request);

      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(COMMENT_ID, result.id()),
              () -> assertEquals("First comment", result.content())
      );

      verify(permissionService)
              .requireCommentCreatePermission(member);

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

  }

  @Test
  void shouldReturnComments() {

      // Arrange
      TaskComment comment = createComment();
      CommentResponse response = createResponse();

      when(taskCommentRepository.findByTaskIdOrderByCreatedAtAsc(TASK_ID))
              .thenReturn(List.of(comment));

      when(taskCommentMapper.toResponse(comment))
              .thenReturn(response);

      // Act
      List<CommentResponse> result = service.getComments(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID);

      // Assert
      assertAll(
              () -> assertEquals(1, result.size()),
              () -> assertEquals(COMMENT_ID, result.get(0).id()),
              () -> assertEquals("First comment", result.get(0).content())
      );

      verify(organizationAuthorizationService)
              .requireOrganizationAccess(
                      ORGANIZATION_ID,
                      USER_ID);

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      verify(taskCommentRepository)
              .findByTaskIdOrderByCreatedAtAsc(TASK_ID);

      verify(taskCommentMapper)
              .toResponse(comment);

  }

  @Test
  void shouldUpdateComment() {

      // Arrange
      UpdateCommentRequest request =
              new UpdateCommentRequest("Updated comment");

      OrganizationMember member = createMember();

      TaskComment comment = createComment();

      CommentResponse response = new CommentResponse(
              COMMENT_ID,
              TASK_ID,
              USER_ID,
              "Updated comment",
              true,
              Instant.now(),
              Instant.now());

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(member);

      when(taskCommentAuthorizationService.requireCommentAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              COMMENT_ID,
              USER_ID))
              .thenReturn(comment);

      when(taskCommentRepository.save(comment))
              .thenReturn(comment);

      when(taskCommentMapper.toResponse(comment))
              .thenReturn(response);

      // Act
      CommentResponse result = service.updateComment(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              COMMENT_ID,
              USER_ID,
              request);

      // Assert
      assertAll(
              () -> assertEquals("Updated comment", result.content()),
              () -> assertTrue(result.edited()),
              () -> assertEquals(COMMENT_ID, result.id())
      );

      assertAll(
              () -> assertEquals("Updated comment", comment.getContent()),
              () -> assertTrue(comment.isEdited())
      );

      verify(permissionService)
              .requireCommentUpdatePermission(
                      member,
                      USER_ID,
                      USER_ID);

      verify(taskCommentRepository)
              .save(comment);

      verify(taskCommentMapper)
              .toResponse(comment);

  }

  @Test
  void shouldDeleteComment() {

      // Arrange
      OrganizationMember member = createMember();

      TaskComment comment = createComment();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(member);

      when(taskCommentAuthorizationService.requireCommentAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              COMMENT_ID,
              USER_ID))
              .thenReturn(comment);

      // Act
      service.deleteComment(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              COMMENT_ID,
              USER_ID);

      // Assert
      verify(permissionService)
              .requireCommentDeletePermission(
                      member,
                      USER_ID,
                      USER_ID);

      verify(taskCommentRepository)
              .delete(comment);

      verify(activityService)
              .logActivity(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      TASK_ID,
                      USER_ID,
                      ActivityEntityType.COMMENT,
                      ActivityAction.DELETED,
                      COMMENT_ID,
                      "Deleted a comment.");

  }
}
