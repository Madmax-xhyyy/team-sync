package com.teamsync.api.features.organizationmember.dto.request;

import com.teamsync.api.features.organization.entity.OrganizationRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

public record AddMemberRequest(

    @Schema(description = "Email", example = "example@gmail.com") @Email @NotBlank String email,

    @Schema(description = "Organization role", example = "MEMBER") @NotNull OrganizationRole role

) {
}
