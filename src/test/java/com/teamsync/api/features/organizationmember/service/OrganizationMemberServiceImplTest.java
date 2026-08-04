package com.teamsync.api.features.organizationmember.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teamsync.api.common.constants.AuthProvider;
import com.teamsync.api.common.constants.Role;
import com.teamsync.api.common.exception.BadRequestException;
import com.teamsync.api.common.exception.NotFoundException;
import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.common.security.PermissionService;
import com.teamsync.api.features.organization.entity.OrganizationRole;
import com.teamsync.api.features.organizationmember.dto.request.AddMemberRequest;
import com.teamsync.api.features.organizationmember.dto.request.UpdateMemberRoleRequest;
import com.teamsync.api.features.organizationmember.dto.response.MemberResponse;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.organizationmember.mapper.OrganizationMemberMapper;
import com.teamsync.api.features.organizationmember.repository.OrganizationMemberRepository;
import com.teamsync.api.features.user.entity.User;
import com.teamsync.api.features.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrganizationMemberServiceImplTest {

  private static final String ORGANIZATION_ID = "org-1";
  private static final String MEMBER_ID = "member-1";
  private static final String USER_ID = "user-1";
  private static final String CURRENT_USER_ID = "owner-1";
  private static final String EMAIL = "john@test.com";

  @Mock
  private UserRepository userRepository;

  @Mock
  private OrganizationMemberRepository organizationMemberRepository;

  @Mock
  private OrganizationAuthorizationService organizationAuthorizationService;

  @Mock
  private PermissionService permissionService;

  @Mock
  private OrganizationMemberMapper organizationMemberMapper;

  @InjectMocks
  private OrganizationMemberServiceImpl service;

  private OrganizationMember createCurrentMember() {

    OrganizationMember member = OrganizationMember.builder()
        .organizationId(ORGANIZATION_ID)
        .userId(CURRENT_USER_ID)
        .role(OrganizationRole.OWNER)
        .joinedAt(Instant.now())
        .build();

    member.setId("current-member");

    return member;
  }

  private OrganizationMember createMember() {

    OrganizationMember member = OrganizationMember.builder()
        .organizationId(ORGANIZATION_ID)
        .userId(USER_ID)
        .role(OrganizationRole.MEMBER)
        .joinedAt(Instant.now())
        .build();

    member.setId(MEMBER_ID);

    return member;
  }

  private User createUser() {

    User user = User.builder()
        .firstName("John")
        .lastName("Doe")
        .email(EMAIL)
        .password("password")
        .role(Role.ROLE_USER)
        .provider(AuthProvider.LOCAL)
        .enabled(true)
        .build();

    user.setId(USER_ID);

    return user;
  }

  private MemberResponse createResponse() {

    return new MemberResponse(
        MEMBER_ID,
        USER_ID,
        "John",
        "Doe",
        EMAIL,
        OrganizationRole.MEMBER);
  }

  @Test
  void shouldAddMember() {

    AddMemberRequest request = new AddMemberRequest(
        EMAIL,
        OrganizationRole.MEMBER);

    OrganizationMember currentMember = createCurrentMember();
    User user = createUser();
    OrganizationMember member = createMember();
    MemberResponse response = createResponse();

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(userRepository.findByEmail(EMAIL))
        .thenReturn(Optional.of(user));

    when(organizationMemberRepository.findByOrganizationIdAndUserId(
        ORGANIZATION_ID,
        USER_ID))
        .thenReturn(Optional.empty());

    when(organizationMemberRepository.save(any(OrganizationMember.class)))
        .thenReturn(member);

    when(organizationMemberMapper.toResponse(
        member,
        user))
        .thenReturn(response);

    MemberResponse result = service.addMember(
        ORGANIZATION_ID,
        CURRENT_USER_ID,
        request);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(USER_ID, result.userId()),
        () -> assertEquals(EMAIL, result.email()),
        () -> assertEquals(OrganizationRole.MEMBER, result.role()));

    verify(permissionService)
        .requireMemberManagementPermission(currentMember);

    verify(organizationMemberRepository)
        .save(any(OrganizationMember.class));
  }

  @Test
  void shouldThrowWhenUserNotFound() {

    AddMemberRequest request = new AddMemberRequest(
        EMAIL,
        OrganizationRole.MEMBER);

    OrganizationMember currentMember = createCurrentMember();

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(userRepository.findByEmail(EMAIL))
        .thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> service.addMember(
            ORGANIZATION_ID,
            CURRENT_USER_ID,
            request));

    verify(permissionService)
        .requireMemberManagementPermission(currentMember);

    verify(userRepository)
        .findByEmail(EMAIL);

    verify(organizationMemberRepository, never())
        .save(any());
  }

  @Test
  void shouldThrowWhenUserAlreadyMember() {

    AddMemberRequest request = new AddMemberRequest(
        EMAIL,
        OrganizationRole.MEMBER);

    OrganizationMember currentMember = createCurrentMember();

    User user = createUser();

    OrganizationMember existingMember = createMember();

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(userRepository.findByEmail(EMAIL))
        .thenReturn(Optional.of(user));

    when(organizationMemberRepository.findByOrganizationIdAndUserId(
        ORGANIZATION_ID,
        USER_ID))
        .thenReturn(Optional.of(existingMember));

    assertThrows(
        BadRequestException.class,
        () -> service.addMember(
            ORGANIZATION_ID,
            CURRENT_USER_ID,
            request));

    verify(permissionService)
        .requireMemberManagementPermission(currentMember);

    verify(organizationMemberRepository, never())
        .save(any());
  }

  @Test
  void shouldGetMembers() {

    // Arrange
    OrganizationMember currentMember = createCurrentMember();

    OrganizationMember member = createMember();

    User user = createUser();

    MemberResponse response = createResponse();

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(organizationMemberRepository.findByOrganizationId(
        ORGANIZATION_ID))
        .thenReturn(List.of(member));

    when(userRepository.findByIdIn(
        List.of(USER_ID)))
        .thenReturn(List.of(user));

    when(organizationMemberMapper.toResponse(
        member,
        user))
        .thenReturn(response);

    // Act
    List<MemberResponse> result = service.getMembers(
        ORGANIZATION_ID,
        CURRENT_USER_ID);

    // Assert
    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(1, result.size()),
        () -> assertEquals(USER_ID, result.get(0).userId()),
        () -> assertEquals(EMAIL, result.get(0).email()),
        () -> assertEquals(OrganizationRole.MEMBER, result.get(0).role()));

    verify(organizationAuthorizationService)
        .requireOrganizationAccess(
            ORGANIZATION_ID,
            CURRENT_USER_ID);

    verify(organizationMemberRepository)
        .findByOrganizationId(ORGANIZATION_ID);

    verify(userRepository)
        .findByIdIn(List.of(USER_ID));

    verify(organizationMemberMapper)
        .toResponse(member, user);
  }

  @Test
  void shouldReturnEmptyMemberList() {

    OrganizationMember currentMember = createCurrentMember();

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(organizationMemberRepository.findByOrganizationId(
        ORGANIZATION_ID))
        .thenReturn(List.of());

    when(userRepository.findByIdIn(List.of()))
        .thenReturn(List.of());

    List<MemberResponse> result = service.getMembers(
        ORGANIZATION_ID,
        CURRENT_USER_ID);

    assertTrue(result.isEmpty());

    verify(organizationAuthorizationService)
        .requireOrganizationAccess(
            ORGANIZATION_ID,
            CURRENT_USER_ID);

    verify(organizationMemberRepository)
        .findByOrganizationId(ORGANIZATION_ID);

    verify(userRepository)
        .findByIdIn(List.of());
  }

  @Test
  void shouldUpdateMemberRole() {

    UpdateMemberRoleRequest request = new UpdateMemberRoleRequest(
        OrganizationRole.ADMIN);

    OrganizationMember currentMember = createCurrentMember();

    OrganizationMember targetMember = createMember();

    User user = createUser();

    MemberResponse response = new MemberResponse(
        MEMBER_ID,
        USER_ID,
        "John",
        "Doe",
        EMAIL,
        OrganizationRole.ADMIN);

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(organizationMemberRepository.findByIdAndOrganizationId(
        MEMBER_ID,
        ORGANIZATION_ID))
        .thenReturn(Optional.of(targetMember));

    when(organizationMemberRepository.save(targetMember))
        .thenReturn(targetMember);

    when(userRepository.findById(USER_ID))
        .thenReturn(Optional.of(user));

    when(organizationMemberMapper.toResponse(
        targetMember,
        user))
        .thenReturn(response);

    MemberResponse result = service.updateRole(
        ORGANIZATION_ID,
        MEMBER_ID,
        CURRENT_USER_ID,
        request);

    assertAll(
        () -> assertNotNull(result),
        () -> assertEquals(USER_ID, result.userId()),
        () -> assertEquals(OrganizationRole.ADMIN, result.role()));

    verify(permissionService)
        .requireRoleUpdatePermission(
            currentMember,
            targetMember,
            OrganizationRole.ADMIN);

    verify(organizationMemberRepository)
        .save(targetMember);
  }

  @Test
  void shouldThrowWhenMemberNotFound() {

    UpdateMemberRoleRequest request = new UpdateMemberRoleRequest(
        OrganizationRole.ADMIN);

    OrganizationMember currentMember = createCurrentMember();

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(organizationMemberRepository.findByIdAndOrganizationId(
        MEMBER_ID,
        ORGANIZATION_ID))
        .thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> service.updateRole(
            ORGANIZATION_ID,
            MEMBER_ID,
            CURRENT_USER_ID,
            request));

    verify(permissionService, never())
        .requireRoleUpdatePermission(
            any(),
            any(),
            any());

    verify(organizationMemberRepository, never())
        .save(any());
  }

  @Test
  void shouldThrowWhenUserNotFoundAfterRoleUpdate() {

    UpdateMemberRoleRequest request = new UpdateMemberRoleRequest(
        OrganizationRole.ADMIN);

    OrganizationMember currentMember = createCurrentMember();

    OrganizationMember targetMember = createMember();

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(organizationMemberRepository.findByIdAndOrganizationId(
        MEMBER_ID,
        ORGANIZATION_ID))
        .thenReturn(Optional.of(targetMember));

    when(organizationMemberRepository.save(targetMember))
        .thenReturn(targetMember);

    when(userRepository.findById(USER_ID))
        .thenReturn(Optional.empty());

    assertThrows(
        NotFoundException.class,
        () -> service.updateRole(
            ORGANIZATION_ID,
            MEMBER_ID,
            CURRENT_USER_ID,
            request));

    verify(permissionService)
        .requireRoleUpdatePermission(
            currentMember,
            targetMember,
            OrganizationRole.ADMIN);

    verify(organizationMemberRepository)
        .save(targetMember);
  }

  @Test
  void shouldRemoveMember() {

    // Arrange
    OrganizationMember currentMember = createCurrentMember();

    OrganizationMember targetMember = createMember();

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(organizationMemberRepository.findByIdAndOrganizationId(
        MEMBER_ID,
        ORGANIZATION_ID))
        .thenReturn(Optional.of(targetMember));

    // Act
    service.removeMember(
        ORGANIZATION_ID,
        MEMBER_ID,
        CURRENT_USER_ID);

    // Assert
    verify(permissionService)
        .requireMemberRemovalPermission(
            currentMember,
            targetMember);

    verify(organizationMemberRepository)
        .delete(targetMember);
  }

  @Test
  void shouldThrowWhenRemovingSelf() {

    // Arrange
    OrganizationMember currentMember = createCurrentMember();

    OrganizationMember targetMember = createCurrentMember();

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(organizationMemberRepository.findByIdAndOrganizationId(
        MEMBER_ID,
        ORGANIZATION_ID))
        .thenReturn(Optional.of(targetMember));

    // Act & Assert
    assertThrows(
        BadRequestException.class,
        () -> service.removeMember(
            ORGANIZATION_ID,
            MEMBER_ID,
            CURRENT_USER_ID));

    verify(permissionService, never())
        .requireMemberRemovalPermission(any(), any());

    verify(organizationMemberRepository, never())
        .delete(any());
  }

  @Test
  void shouldThrowWhenTargetMemberNotFound() {

    // Arrange
    OrganizationMember currentMember = createCurrentMember();

    when(organizationAuthorizationService.requireOrganizationAccess(
        ORGANIZATION_ID,
        CURRENT_USER_ID))
        .thenReturn(currentMember);

    when(organizationMemberRepository.findByIdAndOrganizationId(
        MEMBER_ID,
        ORGANIZATION_ID))
        .thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        NotFoundException.class,
        () -> service.removeMember(
            ORGANIZATION_ID,
            MEMBER_ID,
            CURRENT_USER_ID));

    verify(permissionService, never())
        .requireMemberRemovalPermission(any(), any());

    verify(organizationMemberRepository, never())
        .delete(any());
  }
}
