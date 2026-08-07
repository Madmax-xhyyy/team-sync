package com.teamsync.api.features.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(

    @Schema(description = "Access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String accessToken,

    @Schema(description = "Token expires in", example = "900") long expiresIn

) {
}
