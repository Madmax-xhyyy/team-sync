package com.teamsync.api.features.organization.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.teamsync.api.features.organization.dto.request.CreateOrganizationRequest;
import com.teamsync.api.features.organization.dto.response.OrganizationResponse;
import com.teamsync.api.features.organization.entity.Organization;

class OrganizationMapperTest {

    private final OrganizationMapper mapper = new OrganizationMapper();

    @Test
    void shouldMapToEntity() {

        CreateOrganizationRequest request =
                new CreateOrganizationRequest(
                        "TeamSync",
                        "Team collaboration platform");

        Organization organization = mapper.toEntity(request);

        assertAll(
                () -> assertEquals("TeamSync", organization.getName()),
                () -> assertEquals(
                        "Team collaboration platform",
                        organization.getDescription())
        );

    }

    @Test
    void shouldMapToResponse() {

        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(60);

        Organization organization = Organization.builder()
                .name("TeamSync")
                .description("Team collaboration platform")
                .build();

        organization.setId("organization-1");
        organization.setCreatedAt(createdAt);
        organization.setUpdatedAt(updatedAt);

        OrganizationResponse response =
                mapper.toResponse(organization);

        assertAll(
                () -> assertEquals("organization-1", response.id()),
                () -> assertEquals("TeamSync", response.name()),
                () -> assertEquals(
                        "Team collaboration platform",
                        response.description()),
                () -> assertEquals(createdAt, response.createdAt()),
                () -> assertEquals(updatedAt, response.updatedAt())
        );

    }

}
