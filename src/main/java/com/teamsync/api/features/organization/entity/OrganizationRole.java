package com.teamsync.api.features.organization.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the role of a user within an organization.")
public enum OrganizationRole {

  @Schema(description = "The user is the owner of the organization.")
  OWNER,

  @Schema(description = "The user is an administrator of the organization.")
  ADMIN,

  @Schema(description = "The user is a member of the organization.")
  MEMBER,

  @Schema(description = "The user is a guest of the organization.")
  GUEST

}
