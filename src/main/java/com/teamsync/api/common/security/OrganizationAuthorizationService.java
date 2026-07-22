package com.teamsync.api.common.security;

import com.teamsync.api.common.domain.organization.OrganizationDomainService;
import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.features.organization.entity.Organization;
import com.teamsync.api.features.organizationmember.repository.OrganizationMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationAuthorizationService {

  private final OrganizationMemberRepository organizationMemberRepository;
  private final OrganizationDomainService organizationDomainService;

  public Organization requireMember(
      String organizationId,
      String userId) {

    organizationMemberRepository
        .findByOrganizationIdAndUserId(
            organizationId,
            userId)
        .orElseThrow(() -> new ForbiddenException(
            "You are not a member of this organization."));

    return organizationDomainService.getById(organizationId);

  }

}