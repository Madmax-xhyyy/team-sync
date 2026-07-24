package com.teamsync.api.features.organizationmember.dto.response;

import com.teamsync.api.features.organization.entity.OrganizationRole;

public record MemberResponse(

        String id,

        String userId,

        String firstName,

        String lastName,

        String email,

        OrganizationRole role

) {}
