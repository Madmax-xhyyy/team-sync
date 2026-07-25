package com.teamsync.api.features.activity.service;

import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.common.security.ProjectAuthorizationService;
import com.teamsync.api.common.security.TaskAuthorizationService;
import com.teamsync.api.features.activity.dto.response.ActivityResponse;
import com.teamsync.api.features.activity.entity.Activity;
import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;
import com.teamsync.api.features.activity.mapper.ActivityMapper;
import com.teamsync.api.features.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

  private final ActivityRepository activityRepository;
  private final ActivityMapper activityMapper;

  private final OrganizationAuthorizationService organizationAuthorizationService;
  private final ProjectAuthorizationService projectAuthorizationService;
  private final TaskAuthorizationService taskAuthorizationService;

  @Override
  @Transactional
  public void logActivity(
      String organizationId,
      String projectId,
      String taskId,
      String userId,
      ActivityEntityType entityType,
      ActivityAction action,
      String entityId,
      String description) {

    Activity activity = activityMapper.toEntity(
        organizationId,
        projectId,
        taskId,
        userId,
        entityType,
        action,
        entityId,
        description);

    activityRepository.save(activity);

  }

  @Override
  @Transactional(readOnly = true)
  public List<ActivityResponse> getOrganizationActivities(
      String organizationId,
      String currentUserId) {

    organizationAuthorizationService.requireOrganizationAccess(
        organizationId,
        currentUserId);

    return activityRepository
        .findByOrganizationIdOrderByCreatedAtDesc(organizationId)
        .stream()
        .map(activityMapper::toResponse)
        .toList();

  }

  @Override
  @Transactional(readOnly = true)
  public List<ActivityResponse> getProjectActivities(
      String organizationId,
      String projectId,
      String currentUserId) {

    projectAuthorizationService.requireProjectAccess(
        organizationId,
        projectId,
        currentUserId);

    return activityRepository
        .findByProjectIdOrderByCreatedAtDesc(projectId)
        .stream()
        .map(activityMapper::toResponse)
        .toList();

  }

  @Override
  @Transactional(readOnly = true)
  public List<ActivityResponse> getTaskActivities(
      String organizationId,
      String projectId,
      String columnId,
      String taskId,
      String currentUserId) {

    taskAuthorizationService.requireTaskAccess(
        organizationId,
        projectId,
        columnId,
        taskId,
        currentUserId);

    return activityRepository
        .findByTaskIdOrderByCreatedAtDesc(taskId)
        .stream()
        .map(activityMapper::toResponse)
        .toList();

  }
}
