package com.teamsync.api.common.security;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teamsync.api.common.domain.task.TaskDomainService;
import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.task.entity.Task;

@ExtendWith(MockitoExtension.class)
class TaskAuthorizationServiceTest {

  private static final String ORGANIZATION_ID = "organization-1";
  private static final String PROJECT_ID = "project-1";
  private static final String COLUMN_ID = "column-1";
  private static final String OTHER_COLUMN_ID = "column-2";
  private static final String TASK_ID = "task-1";
  private static final String USER_ID = "user-1";

  @Mock
  private TaskColumnAuthorizationService taskColumnAuthorizationService;

  @Mock
  private TaskDomainService taskDomainService;

  @InjectMocks
  private TaskAuthorizationService authorizationService;

  private Task createTask() {

    Task task = Task.builder()
            .projectId(PROJECT_ID)
            .columnId(COLUMN_ID)
            .title("Fix login")
            .description("Fix login bug")
            .position(1)
            .build();

    task.setId(TASK_ID);

    return task;
  }

  @Test
  void shouldReturnTask() {

      // Arrange
      Task task = createTask();

      when(taskDomainService.getById(TASK_ID))
              .thenReturn(task);

      // Act
      Task result = authorizationService.requireTaskAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              COLUMN_ID,
              TASK_ID,
              USER_ID);

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(TASK_ID, result.getId()),
              () -> assertEquals(COLUMN_ID, result.getColumnId())
      );

      verify(taskColumnAuthorizationService)
              .requireTaskColumnAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID);

      verify(taskDomainService)
              .getById(TASK_ID);

      verifyNoMoreInteractions(
              taskColumnAuthorizationService,
              taskDomainService);
  }

  @Test
  void shouldThrowWhenTaskBelongsToDifferentColumn() {

      // Arrange
      Task task = createTask();
      task.setColumnId(OTHER_COLUMN_ID);

      when(taskDomainService.getById(TASK_ID))
              .thenReturn(task);

      // Act
      ForbiddenException exception = assertThrows(
              ForbiddenException.class,
              () -> authorizationService.requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID));

      // Assert
      assertEquals(
              "Task does not belong to this column.",
              exception.getMessage());

  }

  @Test
  void shouldPropagateForbiddenWhenTaskColumnAccessFails() {

      // Arrange
      doThrow(new ForbiddenException("Access denied"))
              .when(taskColumnAuthorizationService)
              .requireTaskColumnAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID);

      // Act
      assertThrows(
              ForbiddenException.class,
              () -> authorizationService.requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID));

      // Assert
      verify(taskDomainService, never())
              .getById(anyString());

  }

  @Test
  void shouldPropagateExceptionWhenTaskDoesNotExist() {

      // Arrange
      when(taskDomainService.getById(TASK_ID))
              .thenThrow(new ResourceNotFoundException(
                      "Task not found."));

      // Act
      assertThrows(
              ResourceNotFoundException.class,
              () -> authorizationService.requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID));

  }
}