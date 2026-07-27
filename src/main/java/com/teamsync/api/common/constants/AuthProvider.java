package com.teamsync.api.common.constants;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the authentication provider.")
public enum AuthProvider {

  @Schema(description = "Local authentication provider")
  LOCAL,

  @Schema(description = "Google authentication provider")
  GOOGLE,

  @Schema(description = "GitHub authentication provider")
  GITHUB

}
