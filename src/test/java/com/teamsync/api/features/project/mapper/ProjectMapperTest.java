package com.teamsync.api.features.project.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.teamsync.api.features.project.dto.request.CreateProjectRequest;
import com.teamsync.api.features.project.dto.response.ProjectResponse;
import com.teamsync.api.features.project.entity.Project;

class ProjectMapperTest {

    private static final String PROJECT_ID = "project-1";
    private static final String ORGANIZATION_ID = "organization-1";
    private static final String USER_ID = "user-1";

    private final ProjectMapper mapper = new ProjectMapper();

    @Test
    void shouldMapToEntity() {

        // Arrange
        CreateProjectRequest request = new CreateProjectRequest(
                "TeamSync API",
                "Backend API for TeamSync");

        // Act
        Project project = mapper.toEntity(
                request,
                ORGANIZATION_ID,
                USER_ID);

        // Assert
        assertAll(
                () -> assertEquals(ORGANIZATION_ID, project.getOrganizationId()),
                () -> assertEquals("TeamSync API", project.getName()),
                () -> assertEquals("Backend API for TeamSync", project.getDescription()),
                () -> assertEquals(USER_ID, project.getCreatedBy())
        );

    }

    @Test
    void shouldMapToResponse() {

        // Arrange
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(60);

        Project project = Project.builder()
                .organizationId(ORGANIZATION_ID)
                .name("TeamSync API")
                .description("Backend API for TeamSync")
                .createdBy(USER_ID)
                .build();

        project.setId(PROJECT_ID);
        project.setCreatedAt(createdAt);
        project.setUpdatedAt(updatedAt);

        // Act
        ProjectResponse response = mapper.toResponse(project);

        // Assert
        assertAll(
                () -> assertEquals(PROJECT_ID, response.id()),
                () -> assertEquals(ORGANIZATION_ID, response.organizationId()),
                () -> assertEquals("TeamSync API", response.name()),
                () -> assertEquals("Backend API for TeamSync", response.description()),
                () -> assertEquals(USER_ID, response.createdBy()),
                () -> assertEquals(createdAt, response.createdAt()),
                () -> assertEquals(updatedAt, response.updatedAt())
        );

    }

}
