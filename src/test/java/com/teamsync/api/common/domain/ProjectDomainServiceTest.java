package com.teamsync.api.common.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.teamsync.api.common.domain.project.ProjectDomainService;
import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.project.entity.Project;
import com.teamsync.api.features.project.repository.ProjectRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectDomainServiceTest {

    private static final String PROJECT_ID = "project-1";

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectDomainService service;

    private Project createProject() {

        Project project = Project.builder()
                .organizationId("organization-1")
                .name("TeamSync")
                .description("Project description")
                .createdBy("user-1")
                .build();

        project.setId(PROJECT_ID);

        return project;
    }

    @Test
    void shouldReturnProjectWhenProjectExists() {

        // Arrange
        Project project = createProject();

        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(Optional.of(project));

        // Act
        Project result = service.getById(PROJECT_ID);

        // Assert
        assertNotNull(result);
        assertEquals(PROJECT_ID, result.getId());

        verify(projectRepository)
                .findById(PROJECT_ID);

    }

    @Test
    void shouldThrowWhenProjectDoesNotExist() {

        // Arrange
        when(projectRepository.findById(PROJECT_ID))
                .thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getById(PROJECT_ID));

        // Assert
        assertEquals(
                "Project not found.",
                exception.getMessage());

        verify(projectRepository)
                .findById(PROJECT_ID);

    }

}
