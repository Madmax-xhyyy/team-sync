package com.teamsync.api.features.organization.service;

import java.time.Instant;

import org.springframework.transaction.annotation.Transactional;

import com.teamsync.api.features.organization.dto.request.CreateOrganizationRequest;
import com.teamsync.api.features.organization.dto.response.OrganizationResponse;
import com.teamsync.api.features.organization.entity.Organization;
import com.teamsync.api.features.organization.entity.OrganizationMember;
import com.teamsync.api.features.organization.entity.OrganizationRole;
import com.teamsync.api.features.organization.mapper.OrganizationMapper;
import com.teamsync.api.features.organization.repository.OrganizationMemberRepository;
import com.teamsync.api.features.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationRepository organizationRepository;
  private final OrganizationMemberRepository organizationMemberRepository;
  private final OrganizationMapper organizationMapper;

  @Override
  @Transactional
  public OrganizationResponse createOrganization(
      String userId,
      CreateOrganizationRequest request) {

    Organization organization = organizationMapper.toEntity(request);

    Organization savedOrganization = organizationRepository.save(organization);

    OrganizationMember owner = OrganizationMember.builder()
        .organizationId(savedOrganization.getId())
        .userId(userId)
        .role(OrganizationRole.OWNER)
        .joinedAt(Instant.now())
        .build();

    organizationMemberRepository.save(owner);

    return organizationMapper.toResponse(savedOrganization);

  }

}
