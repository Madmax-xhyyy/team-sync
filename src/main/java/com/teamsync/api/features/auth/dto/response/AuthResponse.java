package com.teamsync.api.features.auth.dto.response;

public record AuthResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn

) {
}
