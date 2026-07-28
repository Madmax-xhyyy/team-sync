package com.teamsync.api.common.security;

import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.features.organization.entity.OrganizationRole;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.organizationmember.repository.OrganizationMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationAuthorizationServiceTest {

    private static final String ORGANIZATION_ID = "organization-1";
    private static final String USER_ID = "user-1";

    @Mock
    private OrganizationMemberRepository organizationMemberRepository;

    @InjectMocks
    private OrganizationAuthorizationService authorizationService;

    @Test
    void shouldReturnOrganizationMember() {

        // Arrange
        OrganizationMember member = createMember();

        when(organizationMemberRepository.findByOrganizationIdAndUserId(
                ORGANIZATION_ID,
                USER_ID))
                .thenReturn(Optional.of(member));

        // Act
        OrganizationMember result =
                authorizationService.requireOrganizationAccess(
                        ORGANIZATION_ID,
                        USER_ID);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(USER_ID, result.getUserId()),
                () -> assertEquals(ORGANIZATION_ID, result.getOrganizationId()),
                () -> assertEquals(OrganizationRole.MEMBER, result.getRole())
        );

        verify(organizationMemberRepository)
                .findByOrganizationIdAndUserId(
                        ORGANIZATION_ID,
                        USER_ID);

        verifyNoMoreInteractions(
                organizationMemberRepository);

    }

    @Test
    void shouldThrowWhenUserIsNotOrganizationMember() {

        // Arrange
        when(organizationMemberRepository.findByOrganizationIdAndUserId(
                ORGANIZATION_ID,
                USER_ID))
                .thenReturn(Optional.empty());

        // Act
        ForbiddenException exception = assertThrows(
                ForbiddenException.class,
                () -> authorizationService.requireOrganizationAccess(
                        ORGANIZATION_ID,
                        USER_ID));

        // Assert
        assertEquals(
                "You are not a member of this organization.",
                exception.getMessage());

        verify(organizationMemberRepository)
                .findByOrganizationIdAndUserId(
                        ORGANIZATION_ID,
                        USER_ID);

        verifyNoMoreInteractions(
                organizationMemberRepository);

    }

    // Helper
    private OrganizationMember createMember() {

        return OrganizationMember.builder()
                .organizationId(ORGANIZATION_ID)
                .userId(USER_ID)
                .role(OrganizationRole.MEMBER)
                .joinedAt(Instant.now())
                .build();

    }

}
