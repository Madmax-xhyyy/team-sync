package com.teamsync.api.features.auth.refresh.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "refresh_tokens")
public class RefreshToken {

  @Id
  private String id;

  private String userId;

  @Indexed(unique = true)
  private String tokenId;

  @Indexed(expireAfter = "0s")
  private Instant expiresAt;

  private boolean revoked;

  private Instant createdAt;

  private Instant revokedAt;
}
