package com.teamsync.api.features.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsync.api.features.auth.dto.request.LoginRequest;
import com.teamsync.api.features.auth.dto.request.RegisterRequest;
import com.teamsync.api.features.auth.dto.response.AuthResponse;
import com.teamsync.api.features.auth.dto.response.LoginResult;
import com.teamsync.api.features.auth.dto.response.RegisterResponse;
import com.teamsync.api.features.auth.security.config.SecurityConfig;
import com.teamsync.api.features.auth.security.filter.JwtAuthenticationFilter;
import com.teamsync.api.features.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
    SecurityConfig.class } // replace with your actual Security configuration class name
))
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AuthService authService;

  @MockitoBean
  private UserDetailsService userDetailsService;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  private RegisterRequest createRegisterRequest() {

    RegisterRequest request = new RegisterRequest();
    request.setFirstName("John");
    request.setLastName("Doe");
    request.setEmail("john@test.com");
    request.setPassword("password123!");

    return request;
  }

  private RegisterResponse createRegisterResponse() {

    return RegisterResponse.builder()
        .id("user-1")
        .firstName("John")
        .lastName("Doe")
        .email("john@test.com")
        .build();
  }

  private LoginRequest createLoginRequest() {

    return new LoginRequest(
        "john@test.com",
        "password123!");
  }

  private AuthResponse createAuthResponse() {

    return new AuthResponse(
        "access-token",
        900L);
  }

  private LoginResult createLoginResult() {

    return new LoginResult(
        "access-token",
        "refresh-token",
        900L);
  }

  @Test
  void shouldRegisterUser() throws Exception {

    RegisterRequest request = createRegisterRequest();

    RegisterResponse response = createRegisterResponse();

    when(authService.register(any(RegisterRequest.class)))
        .thenReturn(response);

    mockMvc.perform(post("/api/v1/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message")
            .value("User registered successfully."))
        .andExpect(jsonPath("$.data.id")
            .value("user-1"))
        .andExpect(jsonPath("$.data.email")
            .value("john@test.com"));
  }

  @Test
  void shouldLogin() throws Exception {

    LoginRequest request = createLoginRequest();

    LoginResult result = createLoginResult();

    when(authService.login(any(LoginRequest.class)))
        .thenReturn(result);

    mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message")
            .value("Login successful."))
        .andExpect(jsonPath("$.data.accessToken")
            .value("access-token"))
        .andExpect(jsonPath("$.data.expiresIn")
            .value(900))
        .andExpect(jsonPath("$.data.refreshToken")
            .doesNotExist())
        .andExpect(jsonPath("$.data.tokenType")
            .doesNotExist())
        .andExpect(cookie().exists("refresh_token"))
        .andExpect(cookie().value("refresh_token", "refresh-token"))
        .andExpect(cookie().httpOnly("refresh_token", true))
        .andExpect(cookie().secure("refresh_token", true));
  }

  @Test
  void shouldRefreshToken() throws Exception {

    LoginResult result = new LoginResult(
        "new-access-token",
        "new-refresh-token",
        900L);

    when(authService.refresh("old-refresh-token"))
        .thenReturn(result);

    mockMvc.perform(post("/api/v1/auth/refresh")
        .cookie(new jakarta.servlet.http.Cookie("refresh_token", "old-refresh-token")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message")
            .value("Access token refreshed successfully."))
        .andExpect(jsonPath("$.data.accessToken")
            .value("new-access-token"))
        .andExpect(jsonPath("$.data.expiresIn")
            .value(900))
        .andExpect(cookie().exists("refresh_token"))
        .andExpect(cookie().value("refresh_token", "new-refresh-token"))
        .andExpect(cookie().httpOnly("refresh_token", true));
  }

  @Test
  void shouldThrowWhenRefreshTokenCookieIsMissing() throws Exception {

    mockMvc.perform(post("/api/v1/auth/refresh"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldLogout() throws Exception {

    mockMvc.perform(post("/api/v1/auth/logout")
        .cookie(new jakarta.servlet.http.Cookie("refresh_token", "refresh-token")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message")
            .value("Logout successful."))
        .andExpect(cookie().exists("refresh_token"))
        .andExpect(cookie().maxAge("refresh_token", 0));

    verify(authService, times(1))
        .logout("refresh-token");
  }

  @Test
  void shouldLogoutEvenIfRefreshTokenCookieIsMissing() throws Exception {

    mockMvc.perform(post("/api/v1/auth/logout"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message")
            .value("Logout successful."))
        .andExpect(cookie().exists("refresh_token"))
        .andExpect(cookie().maxAge("refresh_token", 0));

    verify(authService, never())
        .logout(any());
  }
}
