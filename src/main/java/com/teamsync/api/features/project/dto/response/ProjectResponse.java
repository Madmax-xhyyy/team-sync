package com.teamsync.api.features.project.dto.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProjectResponse(

    @Schema(description = "Project ID", example = "60d5fecb9c9c9d001a1c1c1c") String id,

    @Schema(description = "Organization ID", example = "60d5fecb9c9c9d001a1c1c1c") String organizationId,

    @Schema(description = "Project name", example = "Mobile App Development") String name,

    @Schema(description = "Project description", example = "Developing a new mobile application") String description,

    @Schema(description = "Project creator", example = "John Doe") String createdBy,

    @Schema(description = "Project creation date", example = "2026-07-27T13:44:17.431Z") Instant createdAt,

    @Schema(description = "Project update date", example = "2026-07-27T13:44:17.431Z") Instant updatedAt

) {
}
