package com.teamsync.api.features.organization.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrganizationRequest(

    @NotBlank(message = "Organization name is required.") @Size(max = 100) String name,

    @Size(max = 500) String description

) {
}