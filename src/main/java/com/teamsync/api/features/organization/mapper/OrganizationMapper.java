package com.teamsync.api.features.organization.mapper;

import com.teamsync.api.features.organization.dto.request.CreateOrganizationRequest;
import com.teamsync.api.features.organization.dto.response.OrganizationResponse;
import com.teamsync.api.features.organization.entity.Organization;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {

  public Organization toEntity(CreateOrganizationRequest request) {

    return Organization.builder()
        .name(request.name())
        .description(request.description())
        .build();
  }

  public OrganizationResponse toResponse(Organization organization) {

    return new OrganizationResponse(
        organization.getId(),
        organization.getName(),
        organization.getDescription(),
        organization.getCreatedAt(),
        organization.getUpdatedAt());

  }
}