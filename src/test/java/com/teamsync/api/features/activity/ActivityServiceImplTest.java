package com.teamsync.api.features.activity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.common.security.ProjectAuthorizationService;
import com.teamsync.api.common.security.TaskAuthorizationService;
import com.teamsync.api.features.activity.dto.response.ActivityResponse;
import com.teamsync.api.features.activity.entity.Activity;
import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;
import com.teamsync.api.features.activity.mapper.ActivityMapper;
import com.teamsync.api.features.activity.repository.ActivityRepository;
import com.teamsync.api.features.activity.service.ActivityServiceImpl;

@ExtendWith(MockitoExtension.class)
class ActivityServiceImplTest {

  private static final String ORGANIZATION_ID = "organization-1";
  private static final String PROJECT_ID = "project-1";
  private static final String COLUMN_ID = "column-1";
  private static final String TASK_ID = "task-1";
  private static final String USER_ID = "user-1";
  private static final String ENTITY_ID = "entity-1";

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityMapper activityMapper;

    @Mock
    private OrganizationAuthorizationService organizationAuthorizationService;

    @Mock
    private ProjectAuthorizationService projectAuthorizationService;

    @Mock
    private TaskAuthorizationService taskAuthorizationService;

    @InjectMocks
    private ActivityServiceImpl activityService;

    private Activity createActivity() {

      Activity activity = Activity.builder()
              .organizationId(ORGANIZATION_ID)
              .projectId(PROJECT_ID)
              .taskId(TASK_ID)
              .userId(USER_ID)
              .entityType(ActivityEntityType.TASK)
              .action(ActivityAction.CREATED)
              .entityId(ENTITY_ID)
              .description("Created task")
              .build();

      activity.setId("activity-1");

      return activity;
  }

  private ActivityResponse createResponse() {

      Instant now = Instant.now();

      return new ActivityResponse(
              "activity-1",
              ORGANIZATION_ID,
              PROJECT_ID,
              TASK_ID,
              USER_ID,
              ActivityEntityType.TASK,
              ActivityAction.CREATED,
              ENTITY_ID,
              "Created task",
              now,
              now
      );

  }

  @Test
  void shouldLogActivity() {

      // Arrange
      Activity activity = createActivity();

      when(activityMapper.toEntity(
              ORGANIZATION_ID,
              PROJECT_ID,
              TASK_ID,
              USER_ID,
              ActivityEntityType.TASK,
              ActivityAction.CREATED,
              ENTITY_ID,
              "Created task"))
              .thenReturn(activity);

      // Act
      activityService.logActivity(
              ORGANIZATION_ID,
              PROJECT_ID,
              TASK_ID,
              USER_ID,
              ActivityEntityType.TASK,
              ActivityAction.CREATED,
              ENTITY_ID,
              "Created task");

      // Assert
      verify(activityMapper)
              .toEntity(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      TASK_ID,
                      USER_ID,
                      ActivityEntityType.TASK,
                      ActivityAction.CREATED,
                      ENTITY_ID,
                      "Created task");

      verify(activityRepository)
              .save(activity);

      verifyNoMoreInteractions(
              activityRepository,
              activityMapper,
              organizationAuthorizationService,
              projectAuthorizationService,
              taskAuthorizationService);

  }

  @Test
  void shouldGetOrganizationActivities() {

      // Arrange
      Activity activity = createActivity();

      ActivityResponse response = createResponse();

      when(activityRepository.findByOrganizationIdOrderByCreatedAtDesc(
              ORGANIZATION_ID))
              .thenReturn(List.of(activity));

      when(activityMapper.toResponse(activity))
              .thenReturn(response);

      // Act
      List<ActivityResponse> result =
              activityService.getOrganizationActivities(
                      ORGANIZATION_ID,
                      USER_ID);

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(1, result.size()),
              () -> assertEquals(response.id(), result.getFirst().id()),
              () -> assertEquals(
                      response.description(),
                      result.getFirst().description())
      );

      verify(organizationAuthorizationService)
              .requireOrganizationAccess(
                      ORGANIZATION_ID,
                      USER_ID);

      verify(activityRepository)
              .findByOrganizationIdOrderByCreatedAtDesc(
                      ORGANIZATION_ID);

      verify(activityMapper)
              .toResponse(activity);

      verifyNoMoreInteractions(
              activityRepository,
              activityMapper,
              organizationAuthorizationService,
              projectAuthorizationService,
              taskAuthorizationService);

  }

  @Test
  void shouldGetProjectActivities() {

      // Arrange
      Activity activity = createActivity();

      ActivityResponse response = createResponse();

      when(activityRepository.findByProjectIdOrderByCreatedAtDesc(
              PROJECT_ID))
              .thenReturn(List.of(activity));

      when(activityMapper.toResponse(activity))
              .thenReturn(response);

      // Act
      List<ActivityResponse> result =
              activityService.getProjectActivities(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      USER_ID);

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(1, result.size()),
              () -> assertEquals(response.id(), result.getFirst().id()),
              () -> assertEquals(
                      response.description(),
                      result.getFirst().description())
      );

      verify(projectAuthorizationService)
              .requireProjectAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      USER_ID);

      verify(activityRepository)
              .findByProjectIdOrderByCreatedAtDesc(
                      PROJECT_ID);

      verify(activityMapper)
              .toResponse(activity);

      verifyNoMoreInteractions(
              activityRepository,
              activityMapper,
              organizationAuthorizationService,
              projectAuthorizationService,
              taskAuthorizationService);

  }

  @Test
  void shouldGetTaskActivities() {

      // Arrange
      Activity activity = createActivity();

      ActivityResponse response = createResponse();

      when(activityRepository.findByTaskIdOrderByCreatedAtDesc(
              TASK_ID))
              .thenReturn(List.of(activity));

      when(activityMapper.toResponse(activity))
              .thenReturn(response);

      // Act
      List<ActivityResponse> result =
              activityService.getTaskActivities(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(1, result.size()),
              () -> assertEquals(response.id(), result.getFirst().id()),
              () -> assertEquals(
                      response.description(),
                      result.getFirst().description())
      );

      verify(taskAuthorizationService)
              .requireTaskAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      COLUMN_ID,
                      TASK_ID,
                      USER_ID);

      verify(activityRepository)
              .findByTaskIdOrderByCreatedAtDesc(
                      TASK_ID);

      verify(activityMapper)
              .toResponse(activity);

      verifyNoMoreInteractions(
              activityRepository,
              activityMapper,
              organizationAuthorizationService,
              projectAuthorizationService,
              taskAuthorizationService);

  }
}
