package com.teamsync.api.features.project.mapper;

import com.teamsync.api.features.project.dto.request.CreateProjectRequest;
import com.teamsync.api.features.project.dto.response.ProjectResponse;
import com.teamsync.api.features.project.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

  public Project toEntity(
      CreateProjectRequest request,
      String organizationId,
      String createdBy) {

    return Project.builder()
        .organizationId(organizationId)
        .name(request.name())
        .description(request.description())
        .createdBy(createdBy)
        .build();
  }

  public ProjectResponse toResponse(Project project) {

    return new ProjectResponse(
        project.getId(),
        project.getOrganizationId(),
        project.getName(),
        project.getDescription(),
        project.getCreatedBy(),
        project.getCreatedAt(),
        project.getUpdatedAt());
  }
}