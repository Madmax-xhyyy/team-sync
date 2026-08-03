package com.teamsync.api.common.security;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teamsync.api.common.domain.taskcomment.TaskCommentDomainService;
import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.taskcomment.entity.TaskComment;

@ExtendWith(MockitoExtension.class)
class TaskCommentAuthorizationServiceTest {

  private static final String ORGANIZATION_ID = "organization-1";
  private static final String PROJECT_ID = "project-1";
  private static final String COLUMN_ID = "column-1";
  private static final String TASK_ID = "task-1";
  private static final String OTHER_TASK_ID = "task-2";
  private static final String COMMENT_ID = "comment-1";
  private static final String USER_ID = "user-1";

  @Mock
  private TaskAuthorizationService taskAuthorizationService;

  @Mock
  private TaskCommentDomainService taskCommentDomainService;

  @InjectMocks
  private TaskCommentAuthorizationService authorizationService;

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

  @Test
  void shouldReturnComment() {

      // Arrange
      TaskComment comment = createComment();

      when(taskCommentDomainService.getById(COMMENT_ID))
              .thenReturn(comment);

      // Act
      TaskComment result = authorizationService.requireCommentAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              COMMENT_ID,
              USER_ID);

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(COMMENT_ID, result.getId()),
              () -> assertEquals(TASK_ID, result.getTaskId())
      );

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      verify(taskCommentDomainService)
              .getById(COMMENT_ID);

  }

  @Test
  void shouldThrowWhenCommentBelongsToDifferentTask() {

      // Arrange
      TaskComment comment = createComment();
      comment.setTaskId(OTHER_TASK_ID);

      when(taskCommentDomainService.getById(COMMENT_ID))
              .thenReturn(comment);

      // Act
      ForbiddenException exception = assertThrows(
              ForbiddenException.class,
              () -> authorizationService.requireCommentAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      COMMENT_ID,
                      USER_ID));

      // Assert
      assertEquals(
              "Comment does not belong to this task.",
              exception.getMessage());

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      verify(taskCommentDomainService)
              .getById(COMMENT_ID);

  }

  @Test
  void shouldPropagateForbiddenWhenTaskAccessFails() {

      // Arrange
      doThrow(new ForbiddenException("Access denied"))
              .when(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      // Act & Assert
      assertThrows(
              ForbiddenException.class,
              () -> authorizationService.requireCommentAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      COMMENT_ID,
                      USER_ID));

      verify(taskCommentDomainService, never())
              .getById(anyString());

  }

  @Test
  void shouldPropagateExceptionWhenCommentDoesNotExist() {

      // Arrange
      when(taskCommentDomainService.getById(COMMENT_ID))
              .thenThrow(new ResourceNotFoundException(
                      "Comment not found."));

      // Act & Assert
      assertThrows(
              ResourceNotFoundException.class,
              () -> authorizationService.requireCommentAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      COMMENT_ID,
                      USER_ID));

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

  }
}
