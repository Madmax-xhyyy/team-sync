package com.teamsync.api.features.project.service;

import com.teamsync.api.common.pagination.PageMapper;
import com.teamsync.api.common.pagination.PageQuery;
import com.teamsync.api.common.pagination.PageResponse;
import com.teamsync.api.common.pagination.SortDirection;
import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;
import com.teamsync.api.features.activity.service.ActivityService;
import com.teamsync.api.features.project.dto.request.CreateProjectRequest;
import com.teamsync.api.features.project.dto.response.ProjectResponse;
import com.teamsync.api.features.project.entity.Project;
import com.teamsync.api.features.project.mapper.ProjectMapper;
import com.teamsync.api.features.project.repository.ProjectRepository;
import com.teamsync.api.features.task.service.TaskColumnService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    private static final String USER_ID = "user-123";
    private static final String ORGANIZATION_ID = "org-123";
    private static final String PROJECT_ID = "project-123";

    private static final String NAME = "TeamSync API";

    private static final String DESCRIPTION =
            "Backend API for TeamSync";

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private OrganizationAuthorizationService authorizationService;

    @Mock
    private TaskColumnService taskColumnService;

    @Mock
    private ActivityService activityService;

    @Mock
    private PageMapper pageMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void shouldCreateProject() {

        // Arrange
        CreateProjectRequest request =
                createRequest();

        Project project =
                createProject();

        Project savedProject =
                createSavedProject();

        ProjectResponse response =
                createResponse();

        when(projectMapper.toEntity(
                request,
                ORGANIZATION_ID,
                USER_ID))
                .thenReturn(project);

        when(projectRepository.save(project))
                .thenReturn(savedProject);

        when(projectMapper.toResponse(savedProject))
                .thenReturn(response);

        // Act
        ProjectResponse result =
                projectService.createProject(
                        ORGANIZATION_ID,
                        USER_ID,
                        request
                );

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(PROJECT_ID, result.id()),
                () -> assertEquals(ORGANIZATION_ID, result.organizationId()),
                () -> assertEquals(NAME, result.name()),
                () -> assertEquals(DESCRIPTION, result.description()),
                () -> assertEquals(USER_ID, result.createdBy())
        );

        verify(authorizationService)
                .requireOrganizationAccess(
                        ORGANIZATION_ID,
                        USER_ID
                );

        verify(projectMapper)
                .toEntity(
                        request,
                        ORGANIZATION_ID,
                        USER_ID
                );

        verify(projectRepository)
                .save(project);

        verify(activityService)
                .logActivity(
                        ORGANIZATION_ID,
                        PROJECT_ID,
                        null,
                        USER_ID,
                        ActivityEntityType.PROJECT,
                        ActivityAction.CREATED,
                        PROJECT_ID,
                        "Created project \"" + NAME + "\""
                );

        verify(taskColumnService)
                .createDefaultColumns(PROJECT_ID);

        verify(projectMapper)
                .toResponse(savedProject);

        verifyNoMoreInteractions(
                projectRepository,
                projectMapper,
                authorizationService,
                taskColumnService,
                activityService,
                pageMapper
        );
    }

    private CreateProjectRequest createRequest() {

        return new CreateProjectRequest(
                NAME,
                DESCRIPTION
        );

    }

    private Project createProject() {

        return Project.builder()
                .organizationId(ORGANIZATION_ID)
                .name(NAME)
                .description(DESCRIPTION)
                .createdBy(USER_ID)
                .build();

    }

    private Project createSavedProject() {

        Project project =
                createProject();

        project.setId(PROJECT_ID);

        return project;

    }

    private ProjectResponse createResponse() {

        Instant now = Instant.now();

        return new ProjectResponse(
                PROJECT_ID,
                ORGANIZATION_ID,
                NAME,
                DESCRIPTION,
                USER_ID,
                now,
                now
        );

    }

    @Test
    void shouldReturnProjectsWhenKeywordIsBlank() {

        // Arrange
        PageQuery pageQuery = new PageQuery(
                0,
                20,
                "createdAt",
                SortDirection.DESC,
                ""
        );

        Project project = createSavedProject();

        Page<Project> page =
                new PageImpl<>(List.of(project));

        PageResponse<ProjectResponse> pageResponse =
                PageResponse.<ProjectResponse>builder()
                        .content(List.of(createResponse()))
                        .page(0)
                        .size(20)
                        .totalElements(1)
                        .totalPages(1)
                        .first(true)
                        .last(true)
                        .build();

        when(projectRepository.findByOrganizationId(
                eq(ORGANIZATION_ID),
                any(Pageable.class)))
                .thenReturn(page);

       when(pageMapper.toResponse(
        eq(page),
        ArgumentMatchers.<Function<Project, ProjectResponse>>any()))
        .thenReturn(pageResponse);

        // Act
        PageResponse<ProjectResponse> result =
                projectService.getProjects(
                        ORGANIZATION_ID,
                        USER_ID,
                        pageQuery
                );

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.content().size()),
                () -> assertEquals(PROJECT_ID, result.content().getFirst().id()),
                () -> assertEquals(NAME, result.content().getFirst().name())
        );

        verify(authorizationService)
                .requireOrganizationAccess(
                        ORGANIZATION_ID,
                        USER_ID);

        verify(projectRepository)
                .findByOrganizationId(
                        eq(ORGANIZATION_ID),
                        any(Pageable.class));

        verify(projectRepository, never())
                .findByOrganizationIdAndNameContainingIgnoreCase(
                        anyString(),
                        anyString(),
                        any(Pageable.class));

        verify(pageMapper)
                .toResponse(
                        eq(page),
                        any());

        verifyNoMoreInteractions(
                projectRepository,
                authorizationService,
                pageMapper,
                activityService,
                taskColumnService,
                projectMapper
        );
    }

    @Test
    void shouldReturnProjectsWhenKeywordIsProvided() {

        // Arrange
        String keyword = "Team";

        PageQuery pageQuery = new PageQuery(
                0,
                20,
                "createdAt",
                SortDirection.DESC,
                keyword
        );

        Project project = createSavedProject();

        Page<Project> page =
                new PageImpl<>(List.of(project));

        PageResponse<ProjectResponse> pageResponse =
                PageResponse.<ProjectResponse>builder()
                        .content(List.of(createResponse()))
                        .page(0)
                        .size(20)
                        .totalElements(1)
                        .totalPages(1)
                        .first(true)
                        .last(true)
                        .build();

        when(projectRepository.findByOrganizationIdAndNameContainingIgnoreCase(
                eq(ORGANIZATION_ID),
                eq(keyword),
                any(Pageable.class)))
                .thenReturn(page);

        when(pageMapper.toResponse(
        eq(page),
        ArgumentMatchers.<Function<Project, ProjectResponse>>any()))
        .thenReturn(pageResponse);

        // Act
        PageResponse<ProjectResponse> result =
                projectService.getProjects(
                        ORGANIZATION_ID,
                        USER_ID,
                        pageQuery
                );

        // Assert
        assertEquals(1, result.content().size());

        verify(authorizationService)
                .requireOrganizationAccess(
                        ORGANIZATION_ID,
                        USER_ID);

        verify(projectRepository)
                .findByOrganizationIdAndNameContainingIgnoreCase(
                        eq(ORGANIZATION_ID),
                        eq(keyword),
                        any(Pageable.class));

        verify(projectRepository, never())
                .findByOrganizationId(
                        anyString(),
                        any(Pageable.class));

        verify(pageMapper)
                .toResponse(
                        eq(page),
                        any());

        verifyNoMoreInteractions(
                projectRepository,
                authorizationService,
                pageMapper,
                activityService,
                taskColumnService,
                projectMapper
        );
    }

}