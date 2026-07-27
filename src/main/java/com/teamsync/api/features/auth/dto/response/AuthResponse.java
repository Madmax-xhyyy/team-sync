package com.teamsync.api.features.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(

    @Schema(description = "Access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String accessToken,

    @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String refreshToken,

    @Schema(description = "Token type", example = "Bearer") String tokenType,

    @Schema(description = "Token expires in", example = "3600") long expiresIn

) {
}
