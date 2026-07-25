package com.teamsync.api.features.activity.service;

import com.teamsync.api.features.activity.dto.response.ActivityResponse;
import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;

import java.util.List;

public interface ActivityService {

  void logActivity(
      String organizationId,
      String projectId,
      String taskId,
      String userId,
      ActivityEntityType entityType,
      ActivityAction action,
      String entityId,
      String description);

  List<ActivityResponse> getOrganizationActivities(
      String organizationId,
      String currentUserId);

  List<ActivityResponse> getProjectActivities(
      String organizationId,
      String projectId,
      String currentUserId);

  List<ActivityResponse> getTaskActivities(
      String organizationId,
      String projectId,
      String columnId,
      String taskId,
      String currentUserId);

}
