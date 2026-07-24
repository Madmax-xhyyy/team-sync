package com.teamsync.api.features.organizationmember.dto.request;

import com.teamsync.api.features.organization.entity.OrganizationRole;

import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(

        @NotNull
        OrganizationRole role

) {}
