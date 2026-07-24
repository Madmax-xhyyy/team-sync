package com.teamsync.api.common.security;

import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.organization.entity.OrganizationRole;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    public void requireProjectManagementPermission(
            OrganizationMember member
    ) {

        if (member.getRole() != OrganizationRole.OWNER &&
            member.getRole() != OrganizationRole.ADMIN) {

            throw new ForbiddenException(
                    "You don't have permission to manage projects."
            );
        }
    }

    public void requireOrganizationManagementPermission(
            OrganizationMember member
    ) {

        if (member.getRole() != OrganizationRole.OWNER) {

            throw new ForbiddenException(
                    "Only the organization owner can perform this action."
            );
        }
    }

    public void requireTaskCreationPermission(
            OrganizationMember member
    ) {

        if (member.getRole() == OrganizationRole.GUEST) {

            throw new ForbiddenException(
                    "Guests cannot create tasks."
            );
        }
    }

    public void requireTaskUpdatePermission(
            OrganizationMember member
    ) {

        if (member.getRole() == OrganizationRole.GUEST) {

            throw new ForbiddenException(
                    "Guests cannot update tasks."
            );
        }
    }

     public void requireProjectViewPermission(
            OrganizationMember member
    ) {

        if (member.getRole() == OrganizationRole.GUEST) {
            throw new ForbiddenException(
                    "You don't have permission to view projects."
            );
        }

    }

    public void requireMemberManagementPermission(
        OrganizationMember member
) {

    if (member.getRole() != OrganizationRole.OWNER &&
        member.getRole() != OrganizationRole.ADMIN) {

        throw new ForbiddenException(
                "You don't have permission to manage organization members."
        );

    }

}

}
