package com.teamsync.api.features.organization.mapper;

import com.teamsync.api.features.organization.dto.request.CreateOrganizationRequest;
import com.teamsync.api.features.organization.dto.response.OrganizationResponse;
import com.teamsync.api.features.organization.entity.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationMapperTest {

  private OrganizationMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new OrganizationMapper();
  }

  @Test
  void shouldMapCreateRequestToEntity() {

    // Arrange
    CreateOrganizationRequest request = new CreateOrganizationRequest(
        "TeamSync",
        "Team collaboration platform");

    // Act
    Organization organization = mapper.toEntity(request);

    // Assert
    assertAll(
        () -> assertNotNull(organization),
        () -> assertEquals("TeamSync", organization.getName()),
        () -> assertEquals(
            "Team collaboration platform",
            organization.getDescription()),
        () -> assertNull(organization.getId()),
        () -> assertNull(organization.getCreatedAt()),
        () -> assertNull(organization.getUpdatedAt()));
  }

  @Test
  void shouldMapEntityToResponse() {

    // Arrange
    Instant now = Instant.now();

    Organization organization = Organization.builder()
        .name("TeamSync")
        .description("Team collaboration platform")
        .build();

    organization.setId("org-123");
    organization.setCreatedAt(now);
    organization.setUpdatedAt(now);

    // Act
    OrganizationResponse response = mapper.toResponse(organization);

    // Assert
    assertAll(
        () -> assertNotNull(response),
        () -> assertEquals("org-123", response.id()),
        () -> assertEquals("TeamSync", response.name()),
        () -> assertEquals(
            "Team collaboration platform",
            response.description()),
        () -> assertEquals(now, response.createdAt()),
        () -> assertEquals(now, response.updatedAt()));
  }

}
