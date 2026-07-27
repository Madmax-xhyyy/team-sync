package com.teamsync.api.features.activity.dto.request;

import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;

import io.swagger.v3.oas.annotations.media.Schema;

public record LogActivityRequest(
    @Schema(description = "Organization ID", example = "1") String organizationId,

    @Schema(description = "Project ID", example = "1") String projectId,

    @Schema(description = "Task ID", example = "1") String taskId,

    @Schema(description = "User ID", example = "1") String userId,

    @Schema(description = "Entity Type", example = "TASK") ActivityEntityType entityType,

    @Schema(description = "Activity Action", example = "CREATE") ActivityAction action,

    @Schema(description = "Entity ID", example = "1") String entityId,

    @Schema(description = "Description", example = "Task created successfully") String description) {
}
