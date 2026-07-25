package com.teamsync.api.common.event.listener;

import com.teamsync.api.common.event.event.TaskCreatedEvent;
import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;
import com.teamsync.api.features.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskActivityListener {

  private final ActivityService activityService;

  @EventListener
  public void handle(TaskCreatedEvent event) {

    activityService.logActivity(
        event.organizationId(),
        event.projectId(),
        event.taskId(),
        event.userId(),
        ActivityEntityType.TASK,
        ActivityAction.CREATED,
        event.taskId(),
        "Created task \"" + event.taskTitle() + "\"");

  }

}
