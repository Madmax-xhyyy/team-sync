package com.teamsync.api.common.domain.project;

import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.project.entity.Project;
import com.teamsync.api.features.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectDomainService {

  private final ProjectRepository projectRepository;

  public Project getById(String projectId) {

    return projectRepository.findById(projectId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Project not found."));
  }

}