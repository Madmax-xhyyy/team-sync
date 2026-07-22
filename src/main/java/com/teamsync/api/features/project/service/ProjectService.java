package com.teamsync.api.features.project.service;

import com.teamsync.api.features.project.dto.request.CreateProjectRequest;
import com.teamsync.api.features.project.dto.response.ProjectResponse;

import java.util.List;

public interface ProjectService {

  ProjectResponse createProject(
      String organizationId,
      String userId,
      CreateProjectRequest request);

  List<ProjectResponse> getProjects(
      String organizationId,
      String userId);

}
