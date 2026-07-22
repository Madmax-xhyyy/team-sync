package com.teamsync.api.common.domain.user;

import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.user.entity.User;
import com.teamsync.api.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {

  private final UserRepository userRepository;

  public User getById(String userId) {

    return userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "User not found."));
  }

}
