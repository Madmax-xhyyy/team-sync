package com.teamsync.api.features.organization.service;

import com.teamsync.api.features.organization.dto.request.CreateOrganizationRequest;
import com.teamsync.api.features.organization.dto.response.OrganizationResponse;

public interface OrganizationService {

  OrganizationResponse createOrganization(
      String userId,
      CreateOrganizationRequest request);

  // OrganizationResponse getOrganization(
  // String organizationId,
  // String userId);

}