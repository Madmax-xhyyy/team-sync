package com.teamsync.api.features.organizationmember.dto.request;

import com.teamsync.api.features.organization.entity.OrganizationRole;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateMemberRoleRequest(

    @Schema(description = "Organization role", example = "MEMBER") @NotNull OrganizationRole role

) {
}
