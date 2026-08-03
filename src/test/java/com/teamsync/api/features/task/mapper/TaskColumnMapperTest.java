package com.teamsync.api.features.task.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.teamsync.api.features.task.dto.response.TaskColumnResponse;
import com.teamsync.api.features.task.entity.TaskColumn;

class TaskColumnMapperTest {

    private final TaskColumnMapper mapper = new TaskColumnMapper();

    @Test
    void shouldMapToResponse() {

        // Arrange
        TaskColumn column = TaskColumn.builder()
                .projectId("project-1")
                .name("To Do")
                .position(1)
                .build();

        column.setId("column-1");

        // Act
        TaskColumnResponse response = mapper.toResponse(column);

        // Assert
        assertAll(
                () -> assertEquals("column-1", response.id()),
                () -> assertEquals("project-1", response.projectId()),
                () -> assertEquals("To Do", response.name()),
                () -> assertEquals(1, response.position())
        );

    }

}