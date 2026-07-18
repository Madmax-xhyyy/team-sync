package com.teamsync.api.features.auth.service;

import com.teamsync.api.common.exception.BadRequestException;
import com.teamsync.api.features.auth.dto.request.RegisterRequest;
import com.teamsync.api.features.auth.dto.response.RegisterResponse;
import com.teamsync.api.features.auth.mapper.UserMapper;
import com.teamsync.api.features.user.entity.User;
import com.teamsync.api.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

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

}