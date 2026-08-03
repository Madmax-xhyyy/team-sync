package com.teamsync.api.features.organization.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.teamsync.api.common.constants.Role;
import com.teamsync.api.features.organization.entity.OrganizationRole;
import com.teamsync.api.features.organizationmember.dto.response.MemberResponse;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.organizationmember.mapper.OrganizationMemberMapper;
import com.teamsync.api.features.user.entity.User;


class OrganizationMemberMapperTest {

    private final OrganizationMemberMapper mapper =
            new OrganizationMemberMapper();

    @Test
    void shouldMapToResponse() {

        // Arrange
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@test.com")
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();

        user.setId("user-1");

        OrganizationMember member = OrganizationMember.builder()
                .organizationId("organization-1")
                .userId(user.getId())
                .role(OrganizationRole.MEMBER)
                .build();

        member.setId("member-1");

        // Act
        MemberResponse response = mapper.toResponse(member, user);

        // Assert
        assertAll(
                () -> assertEquals("member-1", response.id()),
                () -> assertEquals("user-1", response.userId()),
                () -> assertEquals("John", response.firstName()),
                () -> assertEquals("Doe", response.lastName()),
                () -> assertEquals("john.doe@test.com", response.email()),
                () -> assertEquals(OrganizationRole.MEMBER, response.role())
        );

    }

}