package com.teamsync.api.features.task.dto.response;

public record TaskColumnResponse(

    String id,

    String projectId,

    String name,

    Integer position

) {
}