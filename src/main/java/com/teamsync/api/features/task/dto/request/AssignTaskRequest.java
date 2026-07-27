package com.teamsync.api.features.task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AssignTaskRequest(

    @Schema(description = "User ID", example = "60d5fecb9c9c9d001a1c1c1c") @NotBlank(message = "User ID is required.") String userId

) {
}
