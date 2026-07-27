package com.teamsync.api.features.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

  @Schema(description = "User ID", example = "1")
  private String id;

  @Schema(description = "First name", example = "John")
  private String firstName;

  @Schema(description = "Last name", example = "Doe")
  private String lastName;

  @Schema(description = "Email", example = "[EMAIL_ADDRESS]")
  private String email;

}