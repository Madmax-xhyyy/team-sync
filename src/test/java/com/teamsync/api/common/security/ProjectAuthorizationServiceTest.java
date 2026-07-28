package com.teamsync.api.common.security;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.teamsync.api.common.domain.project.ProjectDomainService;
import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.features.organization.entity.OrganizationRole;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.project.entity.Project;

@ExtendWith(MockitoExtension.class)
class ProjectAuthorizationServiceTest {

    private static final String ORGANIZATION_ID = "organization-1";
    private static final String OTHER_ORGANIZATION_ID = "organization-2";
    private static final String PROJECT_ID = "project-1";
    private static final String USER_ID = "user-1";

    @Mock
    private OrganizationAuthorizationService organizationAuthorizationService;

    @Mock
    private ProjectDomainService projectDomainService;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private ProjectAuthorizationService authorizationService;

    private OrganizationMember createMember() {

    return OrganizationMember.builder()
            .organizationId(ORGANIZATION_ID)
            .userId(USER_ID)
            .role(OrganizationRole.MEMBER)
            .joinedAt(Instant.now())
            .build();

    }

    private Project createProject() {

        Project project = Project.builder()
                .organizationId(ORGANIZATION_ID)
                .name("Website")
                .description("Website project")
                .createdBy(USER_ID)
                .build();

        project.setId(PROJECT_ID);

        return project;

    }

    @Test
  void shouldReturnProject() {

      OrganizationMember member = createMember();
      Project project = createProject();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(member);

      when(projectDomainService.getById(PROJECT_ID))
              .thenReturn(project);

      Project result = authorizationService.requireProjectAccess(
              ORGANIZATION_ID,
              PROJECT_ID,
              USER_ID);

      assertAll(
              () -> assertNotNull(result),
              () -> assertEquals(PROJECT_ID, result.getId()),
              () -> assertEquals(ORGANIZATION_ID, result.getOrganizationId())
      );

      verify(organizationAuthorizationService)
              .requireOrganizationAccess(
                      ORGANIZATION_ID,
                      USER_ID);

      verify(permissionService)
              .requireProjectViewPermission(member);

      verify(projectDomainService)
              .getById(PROJECT_ID);

      verifyNoMoreInteractions(
              organizationAuthorizationService,
              permissionService,
              projectDomainService);

  }

  @Test
  void shouldThrowWhenProjectBelongsToDifferentOrganization() {

      OrganizationMember member = createMember();

      Project project = createProject();
      project.setOrganizationId(OTHER_ORGANIZATION_ID);

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(member);

      when(projectDomainService.getById(PROJECT_ID))
              .thenReturn(project);

      ForbiddenException exception = assertThrows(
              ForbiddenException.class,
              () -> authorizationService.requireProjectAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      USER_ID));

      assertEquals(
              "Project does not belong to this organization.",
              exception.getMessage());

  }

  @Test
  void shouldPropagateForbiddenWhenOrganizationAccessFails() {

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenThrow(new ForbiddenException("Access denied"));

      assertThrows(
              ForbiddenException.class,
              () -> authorizationService.requireProjectAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      USER_ID));

      verify(projectDomainService, never()).getById(anyString());

  }

  @Test
  void shouldPropagateForbiddenWhenProjectViewPermissionFails() {

      OrganizationMember member = createMember();

      when(organizationAuthorizationService.requireOrganizationAccess(
              ORGANIZATION_ID,
              USER_ID))
              .thenReturn(member);

      doThrow(new ForbiddenException("Permission denied"))
              .when(permissionService)
              .requireProjectViewPermission(member);

      assertThrows(
              ForbiddenException.class,
              () -> authorizationService.requireProjectAccess(
                      ORGANIZATION_ID,
                      PROJECT_ID,
                      USER_ID));

      verify(projectDomainService, never()).getById(anyString());

  }
}