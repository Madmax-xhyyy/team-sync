package com.teamsync.api.features.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.teamsync.api.common.constants.AuthProvider;
import com.teamsync.api.common.constants.Role;
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

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  private static final String EMAIL = "john@test.com";
  private static final String PASSWORD = "password";
  private static final String USER_ID = "user-1";

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private AuthMapper userMapper;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private JwtService jwtService;

  @Mock
  private Authentication authentication;

  @InjectMocks
  private AuthServiceImpl service;

  private User createUser() {

    User user = User.builder()
        .firstName("John")
        .lastName("Doe")
        .email(EMAIL)
        .password(PASSWORD)
        .role(Role.ROLE_USER)
        .provider(AuthProvider.LOCAL)
        .enabled(true)
        .build();

    user.setId(USER_ID);

    return user;
  }

  private RegisterRequest createRegisterRequest() {

    RegisterRequest request = new RegisterRequest();

    request.setFirstName("John");
    request.setLastName("Doe");
    request.setEmail(EMAIL);
    request.setPassword(PASSWORD);

    return request;
  }

  private RegisterResponse createRegisterResponse() {

    return RegisterResponse.builder()
        .id(USER_ID)
        .firstName("John")
        .lastName("Doe")
        .email(EMAIL)
        .build();

  }

  @Test
  void shouldRegisterUser() {

    RegisterRequest request = createRegisterRequest();

    User user = createUser();

    RegisterResponse response = createRegisterResponse();

    when(userRepository.existsByEmail(EMAIL))
        .thenReturn(false);

    when(userMapper.toEntity(request))
        .thenReturn(user);

    when(passwordEncoder.encode(PASSWORD))
        .thenReturn("encoded-password");

    when(userRepository.save(user))
        .thenReturn(user);

    when(userMapper.toRegisterResponse(user))
        .thenReturn(response);

    RegisterResponse result = service.register(request);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(USER_ID, result.getId()),
        () -> assertEquals(EMAIL, result.getEmail()));

    verify(passwordEncoder)
        .encode(PASSWORD);

    verify(userRepository)
        .save(user);
  }

  @Test
  void shouldThrowWhenEmailAlreadyExists() {

    RegisterRequest request = createRegisterRequest();

    when(userRepository.existsByEmail(EMAIL))
        .thenReturn(true);

    assertThrows(
        BadRequestException.class,
        () -> service.register(request));

    verify(userRepository, never())
        .save(any());

    verify(passwordEncoder, never())
        .encode(any());
  }

  private LoginRequest createLoginRequest() {

    return new LoginRequest(
        EMAIL,
        PASSWORD);

  }

  @Test
  void shouldLoginSuccessfully() {

    LoginRequest request = createLoginRequest();

    User user = createUser();

    CustomUserDetails userDetails = new CustomUserDetails(user);

    when(authenticationManager.authenticate(any(
        UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);

    when(authentication.getPrincipal())
        .thenReturn(userDetails);

    when(jwtService.generateAccessToken(userDetails))
        .thenReturn("access-token");

    when(jwtService.generateRefreshToken(userDetails))
        .thenReturn("refresh-token");

    when(jwtService.getAccessTokenExpiration())
        .thenReturn(900000L);

    AuthResponse response = service.login(request);

    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals("access-token", response.accessToken()),
        () -> assertEquals("refresh-token", response.refreshToken()),
        () -> assertEquals("Bearer", response.tokenType()),
        () -> assertEquals(900000L, response.expiresIn()));

    verify(authenticationManager)
        .authenticate(any(
            UsernamePasswordAuthenticationToken.class));

    verify(jwtService)
        .generateAccessToken(userDetails);

    verify(jwtService)
        .generateRefreshToken(userDetails);
  }

  @Test
  void shouldPropagateAuthenticationFailure() {

    LoginRequest request = createLoginRequest();

    when(authenticationManager.authenticate(any(
        UsernamePasswordAuthenticationToken.class)))
        .thenThrow(new RuntimeException("Authentication failed"));

    RuntimeException ex = assertThrows(
        RuntimeException.class,
        () -> service.login(request));

    assertEquals(
        "Authentication failed",
        ex.getMessage());

    verify(jwtService, never())
        .generateAccessToken(any());

    verify(jwtService, never())
        .generateRefreshToken(any());
  }

  @Test
  void shouldEncodePasswordBeforeSaving() {

    RegisterRequest request = createRegisterRequest();

    User user = createUser();

    RegisterResponse response = createRegisterResponse();

    when(userRepository.existsByEmail(EMAIL))
        .thenReturn(false);

    when(userMapper.toEntity(request))
        .thenReturn(user);

    when(passwordEncoder.encode(PASSWORD))
        .thenReturn("encoded-password");

    when(userRepository.save(user))
        .thenReturn(user);

    when(userMapper.toRegisterResponse(user))
        .thenReturn(response);

    service.register(request);

    verify(passwordEncoder)
        .encode(PASSWORD);

    assertEquals(
        "encoded-password",
        user.getPassword());
  }

  @Test
  void shouldGenerateBothTokens() {

    LoginRequest request = createLoginRequest();

    User user = createUser();

    CustomUserDetails userDetails = new CustomUserDetails(user);

    when(authenticationManager.authenticate(any(
        UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);

    when(authentication.getPrincipal())
        .thenReturn(userDetails);

    when(jwtService.generateAccessToken(any()))
        .thenReturn("access");

    when(jwtService.generateRefreshToken(any()))
        .thenReturn("refresh");

    when(jwtService.getAccessTokenExpiration())
        .thenReturn(900000L);

    service.login(request);

    verify(jwtService, times(1))
        .generateAccessToken(userDetails);

    verify(jwtService, times(1))
        .generateRefreshToken(userDetails);

    verify(jwtService, times(1))
        .getAccessTokenExpiration();
  }
}
