package com.teamsync.api.common.domain.organization;

import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.organization.entity.Organization;
import com.teamsync.api.features.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationDomainService {

  private final OrganizationRepository organizationRepository;

  public Organization getById(String organizationId) {

    return organizationRepository.findById(organizationId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Organization not found."));
  }

}