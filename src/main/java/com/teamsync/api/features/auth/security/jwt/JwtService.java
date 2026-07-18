package com.teamsync.api.features.auth.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtProperties jwtProperties;
  private SecretKey secretKey;

  @PostConstruct
  void init() {
      this.secretKey = Keys.hmacShaKeyFor(
              jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
      );
  }
    
  private String buildToken( CustomUserDetails userDetails, long expiration) {
    Date now = new Date();
    return Jwts.builder()
            .subject(userDetails.getUsername())
            .claim("uid", userDetails.getUserId())
            .issuer(jwtProperties.getIssuer())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expiration))
            .signWith(secretKey)
            .compact();
  }

  public String generateAccessToken(CustomUserDetails userDetails) {
    return buildToken(
            userDetails,
            jwtProperties.getAccessTokenExpiration()
    );
  }

  public String generateRefreshToken(CustomUserDetails userDetails) {
    return buildToken(
            userDetails,
            jwtProperties.getRefreshTokenExpiration()
    );
  }

  public String extractUserId(String token) {
    return extractClaim(
            token,
            claims -> claims.get("uid", String.class)
    );
 }
    
  public long getAccessTokenExpiration() {
      return jwtProperties.getAccessTokenExpiration();
  }

  public <T> T extractClaim(String token, Function<Claims, T> resolver) {
    Claims claims = extractClaims(token);
    return resolver.apply(claims);
  }

  public String extractUsername(String token) {
    return extractClaim(
            token,
            Claims::getSubject
    );
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    try{
      return extractUsername(token)
            .equals(userDetails.getUsername())
            && !isTokenExpired(token);
    }catch (Exception ex) {
      return false;
    }
  }

  public Date extractExpiration(String token) {
    return extractClaim(
            token,
            Claims::getExpiration
    );
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token)
            .before(new Date());
  }

  private Claims extractClaims(String token) {
    return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }
}
