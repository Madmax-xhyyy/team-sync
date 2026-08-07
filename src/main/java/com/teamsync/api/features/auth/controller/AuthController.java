package com.teamsync.api.features.auth.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.dto.request.LoginRequest;
import com.teamsync.api.features.auth.dto.request.RegisterRequest;
import com.teamsync.api.features.auth.dto.response.AuthResponse;
import com.teamsync.api.features.auth.dto.response.LoginResult;
import com.teamsync.api.features.auth.dto.response.RegisterResponse;
import com.teamsync.api.features.auth.service.AuthService;

import java.time.Duration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Register a new user")
  @PostMapping("/register")
  public ApiResponse<RegisterResponse> register(
      @Valid @RequestBody RegisterRequest request) {

    RegisterResponse response = authService.register(request);

    return ApiResponse.<RegisterResponse>builder()
        .success(true)
        .message("User registered successfully.")
        .data(response)
        .build();
  }

  @Operation(summary = "Login")
  @PostMapping("/login")
  public ApiResponse<AuthResponse> login(
      @Valid @RequestBody LoginRequest request,
      HttpServletResponse response) {

    LoginResult result = authService.login(request);

    ResponseCookie refreshCookie = ResponseCookie
        .from("refresh_token", result.refreshToken())
        .httpOnly(true)
        .secure(true)
        .sameSite("Strict")
        .path("/api/v1/auth")
        .maxAge(Duration.ofDays(7))
        .build();

    response.addHeader(
        HttpHeaders.SET_COOKIE,
        refreshCookie.toString());

    AuthResponse authResponse = new AuthResponse(
        result.accessToken(),
        result.expiresIn());

    return ApiResponse.<AuthResponse>builder()
        .success(true)
        .message("Login successful.")
        .data(authResponse)
        .build();
  }
}
