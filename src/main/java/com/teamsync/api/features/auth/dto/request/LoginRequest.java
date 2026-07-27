package com.teamsync.api.features.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(

    @Schema(description = "Email", example = "example@gmail.com") @NotBlank @Email String email,

    @Schema(description = "Password", example = "password123!") @NotBlank String password

) {
}
