package com.teamsync.api.features.activity.repository;

import com.teamsync.api.features.activity.entity.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityRepository
    extends MongoRepository<Activity, String> {

  List<Activity> findByOrganizationIdOrderByCreatedAtDesc(
      String organizationId);

  List<Activity> findByProjectIdOrderByCreatedAtDesc(
      String projectId);

  List<Activity> findByTaskIdOrderByCreatedAtDesc(
      String taskId);

}