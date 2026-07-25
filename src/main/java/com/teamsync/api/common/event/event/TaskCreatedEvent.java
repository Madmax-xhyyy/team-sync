package com.teamsync.api.common.event.event;

public record TaskCreatedEvent(

    String organizationId,

    String projectId,

    String taskId,

    String userId,

    String taskTitle

) {
}