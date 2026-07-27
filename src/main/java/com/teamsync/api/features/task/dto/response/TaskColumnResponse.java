package com.teamsync.api.features.task.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TaskColumnResponse(

    @Schema(description = "Task column ID", example = "60d5fecb9c9c9d001a1c1c1c") String id,

    @Schema(description = "Project ID", example = "60d5fecb9c9c9d001a1c1c1c") String projectId,

    @Schema(description = "Column name", example = "To Do") String name,

    @Schema(description = "Column position", example = "0") Integer position

) {
}