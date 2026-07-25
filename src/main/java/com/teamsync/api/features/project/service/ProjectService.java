package com.teamsync.api.features.project.service;

import com.teamsync.api.common.pagination.PageResponse;
import com.teamsync.api.common.pagination.PageQuery;
import com.teamsync.api.features.project.dto.request.CreateProjectRequest;
import com.teamsync.api.features.project.dto.response.ProjectResponse;

public interface ProjectService {

    ProjectResponse createProject(
            String organizationId,
            String userId,
            CreateProjectRequest request);

    PageResponse<ProjectResponse> getProjects(
            String organizationId,
            String userId,
            PageQuery pagination);

}
