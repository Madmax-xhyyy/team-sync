package com.teamsync.api.features.auth.dto.response;

public record LoginResult(
    String accessToken,
    String refreshToken,
    long expiresIn) {
}
