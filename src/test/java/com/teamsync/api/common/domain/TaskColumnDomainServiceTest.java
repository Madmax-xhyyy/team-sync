package com.teamsync.api.common.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.teamsync.api.common.domain.task.TaskColumnDomainService;
import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.task.entity.TaskColumn;
import com.teamsync.api.features.task.repository.TaskColumnRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskColumnDomainServiceTest {

    private static final String COLUMN_ID = "column-1";

    @Mock
    private TaskColumnRepository taskColumnRepository;

    @InjectMocks
    private TaskColumnDomainService service;

    private TaskColumn createColumn() {

        TaskColumn column = TaskColumn.builder()
                .projectId("project-1")
                .name("Todo")
                .position(1)
                .build();

        column.setId(COLUMN_ID);

        return column;
    }

    @Test
    void shouldReturnColumnWhenColumnExists() {

        // Arrange
        TaskColumn column = createColumn();

        when(taskColumnRepository.findById(COLUMN_ID))
                .thenReturn(Optional.of(column));

        // Act
        TaskColumn result = service.getById(COLUMN_ID);

        // Assert
        assertNotNull(result);
        assertEquals(COLUMN_ID, result.getId());

        verify(taskColumnRepository)
                .findById(COLUMN_ID);

    }

    @Test
    void shouldThrowWhenColumnDoesNotExist() {

        // Arrange
        when(taskColumnRepository.findById(COLUMN_ID))
                .thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getById(COLUMN_ID));

        // Assert
        assertEquals(
                "Task column not found.",
                exception.getMessage());

        verify(taskColumnRepository)
                .findById(COLUMN_ID);

    }

}
