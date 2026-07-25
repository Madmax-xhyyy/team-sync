package com.teamsync.api.features.activity.mapper;

import com.teamsync.api.features.activity.dto.response.ActivityResponse;
import com.teamsync.api.features.activity.entity.Activity;
import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

  public Activity toEntity(
      String organizationId,
      String projectId,
      String taskId,
      String userId,
      ActivityEntityType entityType,
      ActivityAction action,
      String entityId,
      String description) {

    return Activity.builder()
        .organizationId(organizationId)
        .projectId(projectId)
        .taskId(taskId)
        .userId(userId)
        .entityType(entityType)
        .action(action)
        .entityId(entityId)
        .description(description)
        .build();

  }

  public ActivityResponse toResponse(
      Activity activity) {

    return new ActivityResponse(
        activity.getId(),
        activity.getOrganizationId(),
        activity.getProjectId(),
        activity.getTaskId(),
        activity.getUserId(),
        activity.getEntityType(),
        activity.getAction(),
        activity.getEntityId(),
        activity.getDescription(),
        activity.getCreatedAt(),
        activity.getUpdatedAt());

  }

}
