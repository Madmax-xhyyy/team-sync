package com.teamsync.api.features.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

  @Schema(description = "First name", example = "John")
  @NotBlank(message = "First name is required.")
  @Size(max = 50, message = "First name must not exceed 50 characters.")
  private String firstName;

  @Schema(description = "Last name", example = "Doe")
  @NotBlank(message = "Last name is required.")
  @Size(max = 50, message = "Last name must not exceed 50 characters.")
  private String lastName;

  @Schema(description = "Email", example = "[EMAIL_ADDRESS]")
  @NotBlank(message = "Email is required.")
  @Email(message = "Please provide a valid email address.")
  private String email;

  @Schema(description = "Password", example = "password123!")
  @NotBlank(message = "Password is required.")
  @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters.")
  private String password;

}
