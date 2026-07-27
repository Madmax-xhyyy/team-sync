package com.teamsync.api.features.organization.dto.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrganizationResponse(

    @Schema(description = "Organization ID", example = "123e4567-e89b-12d3-a456-426614174000") String id,

    @Schema(description = "Organization name", example = "Acme Corporation") String name,

    @Schema(description = "Organization description", example = "Internal collaboration workspace") String description,

    @Schema(description = "Organization creation date", example = "2023-10-26T10:00:00Z") Instant createdAt,

    @Schema(description = "Organization update date", example = "2023-10-26T10:00:00Z") Instant updatedAt

) {
}