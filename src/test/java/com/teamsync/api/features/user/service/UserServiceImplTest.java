package com.teamsync.api.features.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teamsync.api.common.constants.AuthProvider;
import com.teamsync.api.common.constants.Role;
import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.user.dto.request.UpdateProfileRequest;
import com.teamsync.api.features.user.dto.response.UserProfileResponse;
import com.teamsync.api.features.user.entity.User;
import com.teamsync.api.features.user.mapper.UserMapper;
import com.teamsync.api.features.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  private static final String USER_ID = "user-1";

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserServiceImpl service;

  private User createUser() {

    User user = User.builder()
        .firstName("John")
        .lastName("Doe")
        .email("john@test.com")
        .password("password")
        .role(Role.ROLE_USER)
        .provider(AuthProvider.LOCAL)
        .enabled(true)
        .emailVerified(true)
        .lastLoginAt(Instant.now())
        .build();

    user.setId(USER_ID);

    return user;
  }

  private UserProfileResponse createResponse() {

    return new UserProfileResponse(
        USER_ID,
        "John",
        "Doe",
        "john@test.com",
        Role.ROLE_USER.name());

  }

  @Test
  void shouldGetCurrentUser() {

    User user = createUser();
    UserProfileResponse response = createResponse();

    when(userRepository.findById(USER_ID))
        .thenReturn(Optional.of(user));

    when(userMapper.toProfileResponse(user))
        .thenReturn(response);

    UserProfileResponse result = service.getCurrentUser(USER_ID);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(USER_ID, result.id()),
        () -> assertEquals("John", result.firstName()),
        () -> assertEquals("Doe", result.lastName()));

    verify(userRepository)
        .findById(USER_ID);

    verify(userMapper)
        .toProfileResponse(user);
  }

  @Test
  void shouldThrowWhenGettingMissingUser() {

    when(userRepository.findById(USER_ID))
        .thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> service.getCurrentUser(USER_ID));

    verify(userMapper, never())
        .toProfileResponse(any());
  }

  @Test
  void shouldUpdateCurrentUser() {

    User user = createUser();

    UpdateProfileRequest request = new UpdateProfileRequest(
        "Jane",
        "Smith");

    UserProfileResponse response = new UserProfileResponse(
        USER_ID,
        "Jane",
        "Smith",
        "john@test.com",
        Role.ROLE_USER.name());

    when(userRepository.findById(USER_ID))
        .thenReturn(Optional.of(user));

    when(userRepository.save(user))
        .thenReturn(user);

    when(userMapper.toProfileResponse(user))
        .thenReturn(response);

    UserProfileResponse result = service.updateCurrentUser(
        USER_ID,
        request);

    assertAll(
        () -> assertEquals("Jane", user.getFirstName()),
        () -> assertEquals("Smith", user.getLastName()),
        () -> assertEquals("Jane", result.firstName()),
        () -> assertEquals("Smith", result.lastName()));

    verify(userRepository)
        .save(user);
  }

  @Test
  void shouldThrowWhenUpdatingMissingUser() {

    UpdateProfileRequest request = new UpdateProfileRequest(
        "Jane",
        "Smith");

    when(userRepository.findById(USER_ID))
        .thenReturn(Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> service.updateCurrentUser(
            USER_ID,
            request));

    verify(userRepository, never())
        .save(any());

    verify(userMapper, never())
        .toProfileResponse(any());
  }
}
