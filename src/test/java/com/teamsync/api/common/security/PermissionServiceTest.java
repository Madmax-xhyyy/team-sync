package com.teamsync.api.common.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.features.organization.entity.OrganizationRole;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;

public class PermissionServiceTest {

 private PermissionService permissionService;

    @BeforeEach
    void setUp() {
        permissionService = new PermissionService();
    }

    private OrganizationMember member(OrganizationRole role) {

        return OrganizationMember.builder()
                .organizationId("organization-1")
                .userId("user-1")
                .role(role)
                .joinedAt(Instant.now())
                .build();

    }

    @Test
    void shouldAllowProjectManagementForOwnerAndAdmin() {

        assertDoesNotThrow(() ->
                permissionService.requireProjectManagementPermission(
                        member(OrganizationRole.OWNER)));

        assertDoesNotThrow(() ->
                permissionService.requireProjectManagementPermission(
                        member(OrganizationRole.ADMIN)));

    }

    @Test
    void shouldDenyProjectManagementForMemberAndGuest() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireProjectManagementPermission(
                        member(OrganizationRole.MEMBER)));

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireProjectManagementPermission(
                        member(OrganizationRole.GUEST)));

    }

    @Test
    void shouldAllowOrganizationManagementOnlyForOwner() {

        assertDoesNotThrow(() ->
                permissionService.requireOrganizationManagementPermission(
                        member(OrganizationRole.OWNER)));

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireOrganizationManagementPermission(
                        member(OrganizationRole.ADMIN)));

    }

    @Test
    void shouldDenyGuestsFromCreatingTasks() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireTaskCreationPermission(
                        member(OrganizationRole.GUEST)));

    }

    @Test
    void shouldAllowMembersToCreateTasks() {

        assertDoesNotThrow(() ->
                permissionService.requireTaskCreationPermission(
                        member(OrganizationRole.MEMBER)));

    }

    @Test
    void shouldDenyGuestsFromUpdatingTasks() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireTaskUpdatePermission(
                        member(OrganizationRole.GUEST)));

    }

    @Test
    void shouldDenyGuestsFromDeletingTasks() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireTaskDeletePermission(
                        member(OrganizationRole.GUEST)));

    }

    @Test
    void shouldDenyGuestsFromAssigningTasks() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireTaskAssignmentPermission(
                        member(OrganizationRole.GUEST)));

    }

    @Test
    void shouldDenyGuestsFromCreatingComments() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireCommentCreatePermission(
                        member(OrganizationRole.GUEST)));

    }

    @Test
    void shouldDenyGuestsFromViewingProjects() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireProjectViewPermission(
                        member(OrganizationRole.GUEST)));

    }

    @Test
    void shouldAllowMemberManagementForOwnerAndAdmin() {

        assertDoesNotThrow(() ->
                permissionService.requireMemberManagementPermission(
                        member(OrganizationRole.OWNER)));

        assertDoesNotThrow(() ->
                permissionService.requireMemberManagementPermission(
                        member(OrganizationRole.ADMIN)));

    }

    @Test
    void shouldDenyMemberManagementForMember() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireMemberManagementPermission(
                        member(OrganizationRole.MEMBER)));

    }

    @Test
    void shouldAllowCommentAuthorToUpdateOwnComment() {

        assertDoesNotThrow(() ->
                permissionService.requireCommentUpdatePermission(
                        member(OrganizationRole.MEMBER),
                        "user-1",
                        "user-1"));

    }

    @Test
    void shouldAllowAdminToUpdateAnyComment() {

        assertDoesNotThrow(() ->
                permissionService.requireCommentUpdatePermission(
                        member(OrganizationRole.ADMIN),
                        "user-2",
                        "user-1"));

    }

    @Test
    void shouldDenyUpdatingOthersComment() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireCommentUpdatePermission(
                        member(OrganizationRole.MEMBER),
                        "user-2",
                        "user-1"));

    }

    @Test
    void shouldAllowCommentAuthorToDeleteOwnComment() {

        assertDoesNotThrow(() ->
                permissionService.requireCommentDeletePermission(
                        member(OrganizationRole.MEMBER),
                        "user-1",
                        "user-1"));

    }

    @Test
    void shouldAllowAdminToDeleteAnyComment() {

        assertDoesNotThrow(() ->
                permissionService.requireCommentDeletePermission(
                        member(OrganizationRole.ADMIN),
                        "user-2",
                        "user-1"));

    }

    @Test
    void shouldDenyDeletingOthersComment() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireCommentDeletePermission(
                        member(OrganizationRole.MEMBER),
                        "user-2",
                        "user-1"));

    }

    @Test
    void shouldAllowOwnerToUpdateRoles() {

        assertDoesNotThrow(() ->
                permissionService.requireRoleUpdatePermission(
                        member(OrganizationRole.OWNER),
                        member(OrganizationRole.MEMBER),
                        OrganizationRole.ADMIN));

    }

    @Test
    void shouldPreventUpdatingOwnerRole() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireRoleUpdatePermission(
                        member(OrganizationRole.ADMIN),
                        member(OrganizationRole.OWNER),
                        OrganizationRole.MEMBER));

    }

    @Test
    void shouldPreventAdminUpdatingAnotherAdmin() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireRoleUpdatePermission(
                        member(OrganizationRole.ADMIN),
                        member(OrganizationRole.ADMIN),
                        OrganizationRole.MEMBER));

    }

    @Test
    void shouldPreventAdminPromotingToAdmin() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireRoleUpdatePermission(
                        member(OrganizationRole.ADMIN),
                        member(OrganizationRole.MEMBER),
                        OrganizationRole.ADMIN));

    }

    @Test
    void shouldAllowOwnerToRemoveMembers() {

        assertDoesNotThrow(() ->
                permissionService.requireMemberRemovalPermission(
                        member(OrganizationRole.OWNER),
                        member(OrganizationRole.MEMBER)));

    }

    @Test
    void shouldAllowAdminToRemoveMember() {

        assertDoesNotThrow(() ->
                permissionService.requireMemberRemovalPermission(
                        member(OrganizationRole.ADMIN),
                        member(OrganizationRole.MEMBER)));

    }

    @Test
    void shouldPreventRemovingOwner() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireMemberRemovalPermission(
                        member(OrganizationRole.ADMIN),
                        member(OrganizationRole.OWNER)));

    }

    @Test
    void shouldPreventAdminRemovingAdmin() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireMemberRemovalPermission(
                        member(OrganizationRole.ADMIN),
                        member(OrganizationRole.ADMIN)));

    }

    @Test
    void shouldPreventMemberRemovingAnyone() {

        assertThrows(
                ForbiddenException.class,
                () -> permissionService.requireMemberRemovalPermission(
                        member(OrganizationRole.MEMBER),
                        member(OrganizationRole.GUEST)));

    }
  
}
