package com.teamsync.api.features.organizationmember.dto.response;

import com.teamsync.api.features.organization.entity.OrganizationRole;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberResponse(

    @Schema(description = "Member ID", example = "1") String id,

    @Schema(description = "User ID", example = "1") String userId,

    @Schema(description = "First name", example = "John") String firstName,

    @Schema(description = "Last name", example = "Doe") String lastName,

    @Schema(description = "Email", example = "example@gmail.com") String email,

    @Schema(description = "Organization role", example = "MEMBER") OrganizationRole role

) {
}
