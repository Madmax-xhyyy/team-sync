package com.teamsync.api.features.auth.security.filter;

import com.teamsync.api.features.auth.security.jwt.JwtService;
import com.teamsync.api.features.auth.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final CustomUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
          HttpServletRequest request,
          HttpServletResponse response,
          FilterChain filterChain
  ) throws ServletException, IOException {

      final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

      // No Authorization header
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
          filterChain.doFilter(request, response);
          return;
      }

      try {

          // Remove "Bearer "
          String jwt = authHeader.substring(7);

          // Extract email from token
          String email = jwtService.extractUsername(jwt);

          // Authenticate only if not already authenticated
          if (email != null &&
                  SecurityContextHolder.getContext().getAuthentication() == null) {

              UserDetails userDetails =
                      userDetailsService.loadUserByUsername(email);

              if (jwtService.isTokenValid(jwt, userDetails)) {

                  UsernamePasswordAuthenticationToken authentication =
                          UsernamePasswordAuthenticationToken.authenticated(
                                  userDetails,
                                  null,
                                  userDetails.getAuthorities()
                          );

                  authentication.setDetails(
                          new WebAuthenticationDetailsSource()
                                  .buildDetails(request)
                  );

                  SecurityContextHolder.getContext()
                          .setAuthentication(authentication);
              }
          }

      } catch (Exception ex) {
          // Invalid JWT
          // Let Spring Security handle the request as unauthenticated.
      }

      filterChain.doFilter(request, response);
  }
}