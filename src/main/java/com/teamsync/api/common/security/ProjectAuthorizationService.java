package com.teamsync.api.common.security;

import com.teamsync.api.common.domain.project.ProjectDomainService;
import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.project.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectAuthorizationService {

  private final OrganizationAuthorizationService organizationAuthorizationService;
  private final ProjectDomainService projectDomainService;
  private final PermissionService permissionService;

  public Project requireProjectAccess(
        String organizationId,
        String projectId,
        String userId
) {

    OrganizationMember member =
            organizationAuthorizationService.requireOrganizationAccess(
                    organizationId,
                    userId
            );

    permissionService.requireProjectViewPermission(member);

    Project project = projectDomainService.getById(projectId);

    if (!project.getOrganizationId().equals(organizationId)) {
        throw new ForbiddenException(
                "Project does not belong to this organization."
        );
    }

    return project;
}
}