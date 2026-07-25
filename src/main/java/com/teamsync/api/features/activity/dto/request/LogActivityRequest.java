package com.teamsync.api.features.activity.dto.request;

import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;

public record LogActivityRequest(
    String organizationId,
    String projectId,
    String taskId,
    String userId,
    ActivityEntityType entityType,
    ActivityAction action,
    String entityId,
    String description) {
}
