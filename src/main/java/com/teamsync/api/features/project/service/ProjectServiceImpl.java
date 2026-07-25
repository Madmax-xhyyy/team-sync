package com.teamsync.api.features.project.service;

import com.teamsync.api.common.pagination.PageMapper;
import com.teamsync.api.common.pagination.PageResponse;
import com.teamsync.api.common.pagination.PageQuery;
import com.teamsync.api.common.pagination.PaginationUtils;
import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.features.activity.entity.ActivityAction;
import com.teamsync.api.features.activity.entity.ActivityEntityType;
import com.teamsync.api.features.activity.service.ActivityService;
import com.teamsync.api.features.project.dto.request.CreateProjectRequest;
import com.teamsync.api.features.project.dto.response.ProjectResponse;
import com.teamsync.api.features.project.entity.Project;
import com.teamsync.api.features.project.mapper.ProjectMapper;
import com.teamsync.api.features.project.repository.ProjectRepository;
import com.teamsync.api.features.task.service.TaskColumnService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final OrganizationAuthorizationService authorizationService;
    private final TaskColumnService taskColumnService;
    private final ActivityService activityService;
    private final PageMapper pageMapper;

    @Override
    public ProjectResponse createProject(
            String organizationId,
            String userId,
            CreateProjectRequest request) {

        authorizationService.requireOrganizationAccess(
                organizationId,
                userId);

        Project project = projectMapper.toEntity(
                request,
                organizationId,
                userId);

        Project savedProject = projectRepository.save(project);

        activityService.logActivity(
                organizationId,
                savedProject.getId(),
                null,
                userId,
                ActivityEntityType.PROJECT,
                ActivityAction.CREATED,
                savedProject.getId(),
                "Created project \"" + savedProject.getName() + "\"");

        taskColumnService.createDefaultColumns(
                savedProject.getId());

        return projectMapper.toResponse(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProjectResponse> getProjects(
            String organizationId,
            String userId,
            PageQuery pagination) {

        authorizationService.requireOrganizationAccess(
                organizationId,
                userId);

        Page<Project> projects;

        if (pagination.keywordOrDefault().isBlank()) {

            projects = projectRepository.findByOrganizationId(
                    organizationId,
                    PaginationUtils.toPageable(pagination));

        } else {

            projects = projectRepository.findByOrganizationIdAndNameContainingIgnoreCase(
                    organizationId,
                    pagination.keywordOrDefault(),
                    PaginationUtils.toPageable(pagination));

        }

        return pageMapper.toResponse(
                projects,
                projectMapper::toResponse);

    }
}
