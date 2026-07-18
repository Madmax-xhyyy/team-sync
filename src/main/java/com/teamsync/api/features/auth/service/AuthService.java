package com.teamsync.api.features.auth.service;

import com.teamsync.api.features.auth.dto.request.LoginRequest;
import com.teamsync.api.features.auth.dto.request.RegisterRequest;
import com.teamsync.api.features.auth.dto.response.AuthResponse;
import com.teamsync.api.features.auth.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}