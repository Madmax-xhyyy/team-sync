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

public void requireRoleUpdatePermission(
        OrganizationMember currentMember,
        OrganizationMember targetMember,
        OrganizationRole newRole
) {

    OrganizationRole currentRole = currentMember.getRole();
    OrganizationRole targetRole = targetMember.getRole();

    // OWNER cannot be modified
    if (targetRole == OrganizationRole.OWNER) {
        throw new ForbiddenException(
                "The organization owner cannot be modified."
        );
    }

    // OWNER can do everything else
    if (currentRole == OrganizationRole.OWNER) {
        return;
    }

    // ADMIN cannot modify another ADMIN
    if (currentRole == OrganizationRole.ADMIN &&
            targetRole == OrganizationRole.ADMIN) {

        throw new ForbiddenException(
                "Admins cannot modify another admin."
        );
    }

    // ADMIN cannot promote to ADMIN
    if (currentRole == OrganizationRole.ADMIN &&
            newRole == OrganizationRole.ADMIN) {

        throw new ForbiddenException(
                "Admins cannot assign the ADMIN role."
        );
    }

    throw new ForbiddenException(
            "You don't have permission to update member roles."
    );
}

public void requireMemberRemovalPermission(
        OrganizationMember currentMember,
        OrganizationMember targetMember
) {

    OrganizationRole currentRole = currentMember.getRole();
    OrganizationRole targetRole = targetMember.getRole();

    // Cannot remove the owner
    if (targetRole == OrganizationRole.OWNER) {
        throw new ForbiddenException(
                "The organization owner cannot be removed."
        );
    }

    // Owner can remove anyone except owner
    if (currentRole == OrganizationRole.OWNER) {
        return;
    }

    // Admin cannot remove another admin
    if (currentRole == OrganizationRole.ADMIN &&
            targetRole == OrganizationRole.ADMIN) {

        throw new ForbiddenException(
                "Admins cannot remove another admin."
        );
    }

    // Admin can remove Member and Guest
    if (currentRole == OrganizationRole.ADMIN) {
        return;
    }

    throw new ForbiddenException(
            "You don't have permission to remove members."
    );

}

}
