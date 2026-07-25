package com.teamsync.api.features.activity.dto.response;

import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;

import java.time.Instant;

public record ActivityResponse(

    String id,

    String organizationId,

    String projectId,

    String taskId,

    String userId,

    ActivityEntityType entityType,

    ActivityAction action,

    String entityId,

    String description,

    Instant createdAt,

    Instant updatedAt

) {
}