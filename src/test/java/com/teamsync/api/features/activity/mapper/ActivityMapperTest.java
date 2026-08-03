package com.teamsync.api.features.activity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.teamsync.api.features.activity.dto.response.ActivityResponse;
import com.teamsync.api.features.activity.entity.Activity;
import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;

class ActivityMapperTest {

    private final ActivityMapper mapper = new ActivityMapper();

    private static final String ORGANIZATION_ID = "organization-1";
    private static final String PROJECT_ID = "project-1";
    private static final String TASK_ID = "task-1";
    private static final String USER_ID = "user-1";
    private static final String ENTITY_ID = "entity-1";
    private static final String DESCRIPTION = "Created task";

    @Test
    void shouldMapToEntity() {

        Activity activity = mapper.toEntity(
                ORGANIZATION_ID,
                PROJECT_ID,
                TASK_ID,
                USER_ID,
                ActivityEntityType.TASK,
                ActivityAction.CREATED,
                ENTITY_ID,
                DESCRIPTION);

        assertAll(
                () -> assertEquals(ORGANIZATION_ID, activity.getOrganizationId()),
                () -> assertEquals(PROJECT_ID, activity.getProjectId()),
                () -> assertEquals(TASK_ID, activity.getTaskId()),
                () -> assertEquals(USER_ID, activity.getUserId()),
                () -> assertEquals(ActivityEntityType.TASK, activity.getEntityType()),
                () -> assertEquals(ActivityAction.CREATED, activity.getAction()),
                () -> assertEquals(ENTITY_ID, activity.getEntityId()),
                () -> assertEquals(DESCRIPTION, activity.getDescription())
        );

    }

    @Test
    void shouldMapToResponse() {

        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(60);

        Activity activity = Activity.builder()
                .organizationId(ORGANIZATION_ID)
                .projectId(PROJECT_ID)
                .taskId(TASK_ID)
                .userId(USER_ID)
                .entityType(ActivityEntityType.TASK)
                .action(ActivityAction.CREATED)
                .entityId(ENTITY_ID)
                .description(DESCRIPTION)
                .build();

        activity.setId("activity-1");
        activity.setCreatedAt(createdAt);
        activity.setUpdatedAt(updatedAt);

        ActivityResponse response = mapper.toResponse(activity);

        assertAll(
                () -> assertEquals("activity-1", response.id()),
                () -> assertEquals(ORGANIZATION_ID, response.organizationId()),
                () -> assertEquals(PROJECT_ID, response.projectId()),
                () -> assertEquals(TASK_ID, response.taskId()),
                () -> assertEquals(USER_ID, response.userId()),
                () -> assertEquals(ActivityEntityType.TASK, response.entityType()),
                () -> assertEquals(ActivityAction.CREATED, response.action()),
                () -> assertEquals(ENTITY_ID, response.entityId()),
                () -> assertEquals(DESCRIPTION, response.description()),
                () -> assertEquals(createdAt, response.createdAt()),
                () -> assertEquals(updatedAt, response.updatedAt())
        );

    }

}
