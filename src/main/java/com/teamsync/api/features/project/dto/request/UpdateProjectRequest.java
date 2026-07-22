package com.teamsync.api.features.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProjectRequest(

    @NotBlank(message = "Project name is required.") @Size(max = 100) String name,

    @Size(max = 500) String description

) {
}
