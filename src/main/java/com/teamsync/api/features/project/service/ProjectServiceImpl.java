package com.teamsync.api.features.project.service;

import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.features.project.dto.request.CreateProjectRequest;
import com.teamsync.api.features.project.dto.response.ProjectResponse;
import com.teamsync.api.features.project.entity.Project;
import com.teamsync.api.features.project.mapper.ProjectMapper;
import com.teamsync.api.features.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectMapper projectMapper;
  private final OrganizationAuthorizationService authorizationService;

  @Override
  public ProjectResponse createProject(
      String organizationId,
      String userId,
      CreateProjectRequest request) {

    authorizationService.requireMember(
        organizationId,
        userId);

    Project project = projectMapper.toEntity(
        request,
        organizationId,
        userId);

    Project savedProject = projectRepository.save(project);

    return projectMapper.toResponse(savedProject);
  }

  @Override
  public List<ProjectResponse> getProjects(
      String organizationId,
      String userId) {

    authorizationService.requireMember(
        organizationId,
        userId);

    return projectRepository
        .findByOrganizationId(organizationId)
        .stream()
        .map(projectMapper::toResponse)
        .toList();
  }
}
