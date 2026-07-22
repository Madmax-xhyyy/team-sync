package com.teamsync.api.features.organization.dto.response;

import java.time.Instant;

public record OrganizationResponse(

        String id,

        String name,

        String description,

        Instant createdAt,

        Instant updatedAt

) {
}