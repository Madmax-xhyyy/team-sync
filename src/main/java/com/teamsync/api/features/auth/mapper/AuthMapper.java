package com.teamsync.api.features.auth.mapper;

import com.teamsync.api.features.auth.dto.request.RegisterRequest;
import com.teamsync.api.features.auth.dto.response.RegisterResponse;
import com.teamsync.api.features.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toEntity(RegisterRequest request) {

      User user = new User();

      user.setFirstName(request.getFirstName());
      user.setLastName(request.getLastName());
      user.setEmail(request.getEmail());

      return user;
    }

    public RegisterResponse toRegisterResponse(User user) {

      return RegisterResponse.builder()
              .id(user.getId())
              .firstName(user.getFirstName())
              .lastName(user.getLastName())
              .email(user.getEmail())
              .build();
    }
}
