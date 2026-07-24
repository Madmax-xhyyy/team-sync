package com.teamsync.api.features.organizationmember.dto.request;

import com.teamsync.api.features.organization.entity.OrganizationRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(

        @Email
        @NotBlank
        String email,

        @NotNull
        OrganizationRole role

) {}
