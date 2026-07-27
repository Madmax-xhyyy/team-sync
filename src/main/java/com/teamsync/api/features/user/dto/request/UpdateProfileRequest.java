package com.teamsync.api.features.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateProfileRequest(

    @Schema(description = "First name", example = "John") @NotBlank @Size(max = 50) String firstName,

    @Schema(description = "Last name", example = "Doe") @NotBlank @Size(max = 50) String lastName

) {
}
