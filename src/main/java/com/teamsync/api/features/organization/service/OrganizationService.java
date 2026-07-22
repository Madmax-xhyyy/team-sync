package com.teamsync.api.features.organization.service;

import java.util.List;

import com.teamsync.api.features.organization.dto.request.CreateOrganizationRequest;
import com.teamsync.api.features.organization.dto.response.OrganizationResponse;

public interface OrganizationService {

  OrganizationResponse createOrganization(
      String userId,
      CreateOrganizationRequest request);

  List<OrganizationResponse> getMyOrganizations(String userId);

  OrganizationResponse getOrganization(
      String organizationId,
      String userId);

}