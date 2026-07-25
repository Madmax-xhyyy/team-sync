package com.teamsync.api.common.domain.activity;

import com.teamsync.api.common.exception.NotFoundException;
import com.teamsync.api.features.activity.entity.Activity;
import com.teamsync.api.features.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityDomainService {

  private final ActivityRepository activityRepository;

  public Activity getById(String activityId) {

    return activityRepository.findById(activityId)
        .orElseThrow(() -> new NotFoundException(
            "Activity not found."));

  }

}
