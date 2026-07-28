package com.teamsync.api.features.task.service;

import com.teamsync.api.common.security.ProjectAuthorizationService;
import com.teamsync.api.features.task.dto.response.TaskColumnResponse;
import com.teamsync.api.features.task.entity.TaskColumn;
import com.teamsync.api.features.task.mapper.TaskColumnMapper;
import com.teamsync.api.features.task.repository.TaskColumnRepository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskColumnServiceImplTest {

    private static final String ORGANIZATION_ID = "org-123";
    private static final String PROJECT_ID = "project-123";
    private static final String USER_ID = "user-123";

    @Mock
    private TaskColumnRepository repository;

    @Mock
    private TaskColumnMapper mapper;

    @Mock
    private ProjectAuthorizationService projectAuthorizationService;

    @InjectMocks
    private TaskColumnServiceImpl taskColumnService;

    @Test
    void shouldCreateDefaultColumns() {

        // Act
        taskColumnService.createDefaultColumns(PROJECT_ID);

        // Assert
        ArgumentCaptor<List<TaskColumn>> captor =
                ArgumentCaptor.forClass(List.class);

        verify(repository)
                .saveAll(captor.capture());

        List<TaskColumn> columns =
                captor.getValue();

        assertAll(

                () -> assertEquals(3, columns.size()),

                () -> assertEquals("Todo",
                        columns.get(0).getName()),

                () -> assertEquals(1,
                        columns.get(0).getPosition()),

                () -> assertEquals(PROJECT_ID,
                        columns.get(0).getProjectId()),

                () -> assertEquals("In Progress",
                        columns.get(1).getName()),

                () -> assertEquals(2,
                        columns.get(1).getPosition()),

                () -> assertEquals("Done",
                        columns.get(2).getName()),

                () -> assertEquals(3,
                        columns.get(2).getPosition())

        );

        verifyNoMoreInteractions(
                repository,
                mapper,
                projectAuthorizationService
        );

    }

    private TaskColumn createColumn() {

    TaskColumn column = TaskColumn.builder()
            .projectId(PROJECT_ID)
            .name("Todo")
            .position(1)
            .build();

    column.setId("column-123");

    return column;

    }

    private TaskColumnResponse createResponse() {

        return new TaskColumnResponse(
                "column-123",
                PROJECT_ID,
                "Todo",
                1
        );

    }

    @Test
    void shouldReturnColumns() {

        // Arrange
        TaskColumn column = createColumn();

        TaskColumnResponse response = createResponse();

        when(repository.findByProjectIdOrderByPositionAsc(PROJECT_ID))
                .thenReturn(List.of(column));

        when(mapper.toResponse(column))
                .thenReturn(response);

        // Act
        List<TaskColumnResponse> result =
                taskColumnService.getColumns(
                        ORGANIZATION_ID,
                        PROJECT_ID,
                        USER_ID
                );

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals("column-123", result.getFirst().id()),
                () -> assertEquals(PROJECT_ID, result.getFirst().projectId()),
                () -> assertEquals("Todo", result.getFirst().name()),
                () -> assertEquals(1, result.getFirst().position())
        );

        verify(projectAuthorizationService)
                .requireProjectAccess(
                        ORGANIZATION_ID,
                        PROJECT_ID,
                        USER_ID
                );

        verify(repository)
                .findByProjectIdOrderByPositionAsc(PROJECT_ID);

        verify(mapper)
                .toResponse(column);

        verifyNoMoreInteractions(
                repository,
                mapper,
                projectAuthorizationService
        );

    }
}
