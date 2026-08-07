package com.teamsync.api.features.auth.refresh.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamsync.api.features.auth.refresh.entity.RefreshToken;

public interface RefreshTokenRepository
    extends MongoRepository<RefreshToken, String> {

  Optional<RefreshToken> findByTokenId(String tokenId);

  void deleteByUserId(String userId);
}
