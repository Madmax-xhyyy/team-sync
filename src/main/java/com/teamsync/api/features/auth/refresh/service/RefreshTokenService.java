package com.teamsync.api.features.auth.refresh.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.teamsync.api.common.exception.BadRequestException;
import com.teamsync.api.features.auth.refresh.entity.RefreshToken;
import com.teamsync.api.features.auth.refresh.repository.RefreshTokenRepository;
import com.teamsync.api.features.auth.security.jwt.JwtService;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;

  public void createSession(
      CustomUserDetails userDetails,
      String refreshToken) {

    String tokenId = jwtService.extractTokenId(refreshToken);

    Instant expiresAt = jwtService.extractExpiration(refreshToken)
        .toInstant();

    RefreshToken session = RefreshToken.builder()
        .userId(userDetails.getUserId())
        .tokenId(tokenId)
        .expiresAt(expiresAt)
        .revoked(false)
        .createdAt(Instant.now())
        .build();

    refreshTokenRepository.save(session);
  }

  public RefreshToken validateSession(String refreshToken) {

    try {
      if (!"refresh".equals(
          jwtService.extractTokenType(refreshToken))) {

        throw new BadRequestException(
            "Invalid refresh token.");
      }

      String tokenId = jwtService.extractTokenId(refreshToken);

      RefreshToken session = refreshTokenRepository
          .findByTokenId(tokenId)
          .orElseThrow(() -> new BadRequestException(
              "Invalid refresh token."));

      if (session.isRevoked()) {
        throw new BadRequestException(
            "Refresh token has been revoked.");
      }

      if (session.getExpiresAt().isBefore(Instant.now())) {
        throw new BadRequestException(
            "Refresh token has expired.");
      }

      return session;

    } catch (BadRequestException ex) {
      throw ex;

    } catch (Exception ex) {
      throw new BadRequestException(
          "Invalid refresh token.");
    }
  }

  public void revokeSession(String tokenId) {

    RefreshToken session = refreshTokenRepository
        .findByTokenId(tokenId)
        .orElseThrow(() -> new BadRequestException(
            "Refresh token not found."));

    session.setRevoked(true);
    session.setRevokedAt(Instant.now());

    refreshTokenRepository.save(session);
  }
}
