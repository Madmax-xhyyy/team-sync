package com.teamsync.api.features.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.dto.request.RegisterRequest;
import com.teamsync.api.features.auth.dto.response.RegisterResponse;
import com.teamsync.api.features.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            @Valid @RequestBody RegisterRequest request
    ) {

        RegisterResponse response = authService.register(request);

        return ApiResponse.<RegisterResponse>builder()
                .success(true)
                .message("User registered successfully.")
                .data(response)
                .build();
    }
}
