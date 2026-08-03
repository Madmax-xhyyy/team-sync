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

import com.teamsync.api.common.domain.task.TaskColumnDomainService;
import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.task.entity.TaskColumn;

@ExtendWith(MockitoExtension.class)
class TaskColumnAuthorizationServiceTest {

  private static final String ORGANIZATION_ID = "organization-1";
  private static final String PROJECT_ID = "project-1";
  private static final String OTHER_PROJECT_ID = "project-2";
  private static final String COLUMN_ID = "column-1";
  private static final String USER_ID = "user-1";

  @Mock
  private ProjectAuthorizationService projectAuthorizationService;

  @Mock
  private TaskColumnDomainService taskColumnDomainService;

  @InjectMocks
  private TaskColumnAuthorizationService authorizationService;

  private TaskColumn createColumn() {

    TaskColumn column = TaskColumn.builder()
            .projectId(PROJECT_ID)
            .name("Todo")
            .position(1)
            .build();

    column.setId(COLUMN_ID);

    return column;
  }

  @Test
  void shouldReturnTaskColumn() {

      // Arrange
      TaskColumn column = createColumn();

      when(taskColumnDomainService.getById(COLUMN_ID))
              .thenReturn(column);

      // Act
      TaskColumn result =
              authorizationService.requireTaskColumnAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID);

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(COLUMN_ID, result.getId()),
              () -> assertEquals(PROJECT_ID, result.getProjectId())
      );

      verify(projectAuthorizationService)
              .requireProjectAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      USER_ID);

      verify(taskColumnDomainService)
              .getById(COLUMN_ID);

      verifyNoMoreInteractions(
              projectAuthorizationService,
              taskColumnDomainService);

  }

  @Test
  void shouldThrowWhenTaskColumnBelongsToDifferentProject() {

      // Arrange
      TaskColumn column = createColumn();
      column.setProjectId(OTHER_PROJECT_ID);

      when(taskColumnDomainService.getById(COLUMN_ID))
              .thenReturn(column);

      // Act
      ForbiddenException exception = assertThrows(
              ForbiddenException.class,
              () -> authorizationService.requireTaskColumnAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID));

      // Assert
      assertEquals(
              "Task column does not belong to this project.",
              exception.getMessage());

  }

  @Test
  void shouldPropagateForbiddenWhenProjectAccessFails() {

      // Arrange
      doThrow(new ForbiddenException("Access denied"))
              .when(projectAuthorizationService)
              .requireProjectAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      USER_ID);

      // Act
      assertThrows(
              ForbiddenException.class,
              () -> authorizationService.requireTaskColumnAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID));

      // Assert
      verify(taskColumnDomainService, never())
              .getById(anyString());

  }

  @Test
  void shouldPropagateExceptionWhenTaskColumnDoesNotExist() {

      // Arrange
      when(taskColumnDomainService.getById(COLUMN_ID))
              .thenThrow(new ResourceNotFoundException(
                      "Task column not found."));

      // Act
      assertThrows(
              ResourceNotFoundException.class,
              () -> authorizationService.requireTaskColumnAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      USER_ID));

  }

}