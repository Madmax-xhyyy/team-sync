package com.teamsync.api.features.auth.service;

import com.teamsync.api.common.exception.BadRequestException;
import com.teamsync.api.features.auth.dto.request.LoginRequest;
import com.teamsync.api.features.auth.dto.request.RegisterRequest;
import com.teamsync.api.features.auth.dto.response.AuthResponse;
import com.teamsync.api.features.auth.dto.response.RegisterResponse;
import com.teamsync.api.features.auth.mapper.AuthMapper;
import com.teamsync.api.features.auth.security.jwt.JwtService;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.user.entity.User;
import com.teamsync.api.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthMapper userMapper;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @Override
  public RegisterResponse register(RegisterRequest request) {

    if (userRepository.existsByEmail(request.getEmail())) {
        throw new BadRequestException("Email is already registered.");
    }

    User user = userMapper.toEntity(request);

    user.setPassword(passwordEncoder.encode(request.getPassword()));

    User savedUser = userRepository.save(user);

    return userMapper.toRegisterResponse(savedUser);
  }
    
  @Override
  public AuthResponse login(LoginRequest request) {

    Authentication authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                    request.email(),
                    request.password()
            )
    );

    CustomUserDetails userDetails =
            (CustomUserDetails) authentication.getPrincipal();

    String accessToken = jwtService.generateAccessToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    return new AuthResponse(
            accessToken,
            refreshToken,
            "Bearer",
            jwtService.getAccessTokenExpiration()
    );
  }

}