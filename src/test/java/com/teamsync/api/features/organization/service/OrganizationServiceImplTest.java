package com.teamsync.api.features.organization.service;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {

  private static final String USER_ID = "user-123";
  private static final String ORGANIZATION_ID = "org-123";
  private static final String NAME = "TeamSync";
  private static final String DESCRIPTION = "Team collaboration platform";

  @Mock
  private OrganizationRepository organizationRepository;

  @Mock
  private OrganizationMemberRepository organizationMemberRepository;

  @Mock
  private OrganizationMapper organizationMapper;

  @Mock
  private OrganizationAuthorizationService authorizationService;

  @InjectMocks
  private OrganizationServiceImpl organizationService;

  @Test
  void shouldCreateOrganization() {

      CreateOrganizationRequest request = createRequest();

      Organization organization = createOrganization();

      Organization savedOrganization = createSavedOrganization();

      OrganizationResponse response = createResponse();

      when(organizationMapper.toEntity(request))
              .thenReturn(organization);

      when(organizationRepository.save(organization))
              .thenReturn(savedOrganization);

      when(organizationMapper.toResponse(savedOrganization))
              .thenReturn(response);

      OrganizationResponse result =
              organizationService.createOrganization(
                      USER_ID,
                      request
              );

      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(ORGANIZATION_ID, result.id()),
              () -> assertEquals(NAME, result.name()),
              () -> assertEquals(DESCRIPTION, result.description())
      );

      verify(organizationRepository).save(organization);

      ArgumentCaptor<OrganizationMember> captor =
              ArgumentCaptor.forClass(OrganizationMember.class);

      verify(organizationMemberRepository)
              .save(captor.capture());

      OrganizationMember owner = captor.getValue();

      assertAll(
              () -> assertEquals(USER_ID, owner.getUserId()),
              () -> assertEquals(ORGANIZATION_ID, owner.getOrganizationId()),
              () -> assertEquals(OrganizationRole.OWNER, owner.getRole()),
              () -> assertNotNull(owner.getJoinedAt())
      );

      verify(organizationMapper).toEntity(request);
      verify(organizationMapper).toResponse(savedOrganization);

      verifyNoMoreInteractions(
              organizationRepository,
              organizationMemberRepository,
              organizationMapper,
              authorizationService
      );
  }

  private CreateOrganizationRequest createRequest() {
      return new CreateOrganizationRequest(
              NAME,
              DESCRIPTION
      );
  }

  private Organization createOrganization() {

      return Organization.builder()
              .name(NAME)
              .description(DESCRIPTION)
              .build();

  }

  private Organization createSavedOrganization() {

      Organization organization =
              createOrganization();

      organization.setId(ORGANIZATION_ID);

      return organization;

  }

  private OrganizationResponse createResponse() {

      return new OrganizationResponse(
              ORGANIZATION_ID,
              NAME,
              DESCRIPTION,
              Instant.now(),
              Instant.now()
      );

  }

  @Test
  void shouldReturnOrganizationsForCurrentUser() {

      // Arrange
      OrganizationMember membership =
              OrganizationMember.builder()
                      .organizationId(ORGANIZATION_ID)
                      .userId(USER_ID)
                      .role(OrganizationRole.OWNER)
                      .build();

      Organization organization =
              createSavedOrganization();

      OrganizationResponse response =
              createResponse();

      when(organizationMemberRepository.findByUserId(USER_ID))
              .thenReturn(List.of(membership));

      when(organizationRepository.findByIdIn(List.of(ORGANIZATION_ID)))
              .thenReturn(List.of(organization));

      when(organizationMapper.toResponse(organization))
              .thenReturn(response);

      // Act
      List<OrganizationResponse> result =
              organizationService.getMyOrganizations(USER_ID);

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(1, result.size()),
              () -> assertEquals(ORGANIZATION_ID, result.getFirst().id()),
              () -> assertEquals(NAME, result.getFirst().name()),
              () -> assertEquals(DESCRIPTION, result.getFirst().description())
      );

      verify(organizationMemberRepository)
              .findByUserId(USER_ID);

      verify(organizationRepository)
              .findByIdIn(List.of(ORGANIZATION_ID));

      verify(organizationMapper)
              .toResponse(organization);

      verifyNoMoreInteractions(
              organizationRepository,
              organizationMemberRepository,
              organizationMapper,
              authorizationService
      );
  }

  @Test
  void shouldReturnEmptyListWhenUserHasNoOrganizations() {

      // Arrange
      when(organizationMemberRepository.findByUserId(USER_ID))
              .thenReturn(List.of());

      when(organizationRepository.findByIdIn(List.of()))
              .thenReturn(List.of());

      // Act
      List<OrganizationResponse> result =
              organizationService.getMyOrganizations(USER_ID);

      // Assert
      assertTrue(result.isEmpty());

      verify(organizationMemberRepository)
              .findByUserId(USER_ID);

      verify(organizationRepository)
              .findByIdIn(List.of());

      verifyNoInteractions(organizationMapper);

      verifyNoMoreInteractions(
              organizationRepository,
              organizationMemberRepository,
              authorizationService
      );
  }

  @Test
  void shouldReturnOrganization() {

      // Arrange
      Organization organization = createSavedOrganization();

      OrganizationResponse response = createResponse();

      when(organizationRepository.findById(ORGANIZATION_ID))
              .thenReturn(Optional.of(organization));

      when(organizationMapper.toResponse(organization))
              .thenReturn(response);

      // Act
      OrganizationResponse result =
              organizationService.getOrganization(
                      ORGANIZATION_ID,
                      USER_ID
              );

      // Assert
      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(ORGANIZATION_ID, result.id()),
              () -> assertEquals(NAME, result.name()),
              () -> assertEquals(DESCRIPTION, result.description())
      );

      verify(authorizationService)
              .requireOrganizationAccess(
                      ORGANIZATION_ID,
                      USER_ID
              );

      verify(organizationRepository)
              .findById(ORGANIZATION_ID);

      verify(organizationMapper)
              .toResponse(organization);

      verifyNoMoreInteractions(
              organizationRepository,
              organizationMemberRepository,
              organizationMapper,
              authorizationService
      );
  }

  @Test
  void shouldThrowExceptionWhenOrganizationDoesNotExist() {

      // Arrange
      when(organizationRepository.findById(ORGANIZATION_ID))
              .thenReturn(Optional.empty());

      // Act & Assert
      ResourceNotFoundException exception =
              assertThrows(
                      ResourceNotFoundException.class,
                      () -> organizationService.getOrganization(
                              ORGANIZATION_ID,
                              USER_ID
                      )
              );

      assertEquals(
              "Organization not found.",
              exception.getMessage()
      );

      verify(authorizationService)
              .requireOrganizationAccess(
                      ORGANIZATION_ID,
                      USER_ID
              );

      verify(organizationRepository)
              .findById(ORGANIZATION_ID);

      verifyNoInteractions(organizationMapper);

      verifyNoMoreInteractions(
              organizationRepository,
              organizationMemberRepository,
              organizationMapper,
              authorizationService
      );
  }

}