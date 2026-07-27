package com.teamsync.api.features.task.dto.request;

import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MoveTaskRequest(

    @Schema(description = "Target column ID", example = "60d5fecb9c9c9d001a1c1c1c") @NotBlank(message = "Target column ID is required.") String targetColumnId,

    @Schema(description = "Position", example = "0") @NotNull(message = "Position is required.") @Min(value = 0, message = "Position must be greater than or equal to 0.") Integer position

) {
}
