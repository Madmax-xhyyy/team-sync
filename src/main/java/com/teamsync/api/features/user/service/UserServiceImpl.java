package com.teamsync.api.features.user.service;

import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.user.dto.request.UpdateProfileRequest;
import com.teamsync.api.features.user.dto.response.UserProfileResponse;
import com.teamsync.api.features.user.entity.User;
import com.teamsync.api.features.user.mapper.UserMapper;
import com.teamsync.api.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserProfileResponse getCurrentUser(String userId) {

      User user = userRepository.findById(userId)
              .orElseThrow(() ->
                      new ResourceNotFoundException("User not found."));

      return userMapper.toProfileResponse(user);
  }

  @Override
  public UserProfileResponse updateCurrentUser(
          String userId,
          UpdateProfileRequest request
  ) {

      User user = userRepository.findById(userId)
              .orElseThrow(() ->
                      new ResourceNotFoundException("User not found."));

      user.setFirstName(request.firstName());
      user.setLastName(request.lastName());

      User updatedUser = userRepository.save(user);

      return userMapper.toProfileResponse(updatedUser);
  }
}
