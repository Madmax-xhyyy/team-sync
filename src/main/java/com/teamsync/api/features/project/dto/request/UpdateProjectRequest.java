package com.teamsync.api.features.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProjectRequest(

    @Schema(description = "Project name", example = "Mobile App Development") @NotBlank(message = "Project name is required.") @Size(max = 100) String name,

    @Schema(description = "Project description", example = "Developing a new mobile application") @Size(max = 500) String description

) {
}
