package com.teamsync.api.features.organization.service;

import java.time.Instant;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.features.organization.dto.request.CreateOrganizationRequest;
import com.teamsync.api.features.organization.dto.response.OrganizationResponse;
import com.teamsync.api.features.organization.entity.Organization;
import com.teamsync.api.features.organization.entity.OrganizationRole;
import com.teamsync.api.features.organization.mapper.OrganizationMapper;
import com.teamsync.api.features.organization.repository.OrganizationRepository;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.organizationmember.repository.OrganizationMemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationRepository organizationRepository;
  private final OrganizationMemberRepository organizationMemberRepository;
  private final OrganizationMapper organizationMapper;
  private final OrganizationAuthorizationService authorizationService;

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

  @Override
  public List<OrganizationResponse> getMyOrganizations(
      String userId) {

    List<OrganizationMember> memberships = organizationMemberRepository.findByUserId(userId);

    List<String> organizationIds = memberships.stream()
        .map(OrganizationMember::getOrganizationId)
        .toList();

    List<Organization> organizations = organizationRepository.findByIdIn(organizationIds);

    return organizations.stream()
        .map(organizationMapper::toResponse)
        .toList();

  }

  @Override
  public OrganizationResponse getOrganization(
      String organizationId,
      String userId) {

    authorizationService.requireMember(
        organizationId,
        userId);

    Organization organization = organizationRepository.findById(organizationId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Organization not found."));

    return organizationMapper.toResponse(organization);

  }

}
