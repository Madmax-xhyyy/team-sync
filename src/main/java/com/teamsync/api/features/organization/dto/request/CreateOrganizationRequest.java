package com.teamsync.api.features.organization.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrganizationRequest(

    @Schema(description = "Organization name", example = "Acme Corporation") @NotBlank(message = "Organization name is required.") @Size(max = 100) String name,

    @Schema(description = "Organization description", example = "Internal collaboration workspace") @Size(max = 500) String description

) {
}