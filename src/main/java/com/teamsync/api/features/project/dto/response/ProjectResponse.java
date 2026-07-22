package com.teamsync.api.features.project.dto.response;

import java.time.Instant;

public record ProjectResponse(

    String id,

    String organizationId,

    String name,

    String description,

    String createdBy,

    Instant createdAt,

    Instant updatedAt

) {
}
