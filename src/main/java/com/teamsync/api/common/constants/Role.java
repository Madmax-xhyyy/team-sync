package com.teamsync.api.common.constants;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the role of a user.")
public enum Role {

  @Schema(description = "Admin role")
  ROLE_ADMIN,

  @Schema(description = "User role")
  ROLE_USER

}
