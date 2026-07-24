package com.teamsync.api.features.task.service;

import com.teamsync.api.common.security.ProjectAuthorizationService;
import com.teamsync.api.features.task.dto.response.TaskColumnResponse;
import com.teamsync.api.features.task.entity.TaskColumn;
import com.teamsync.api.features.task.mapper.TaskColumnMapper;
import com.teamsync.api.features.task.repository.TaskColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskColumnServiceImpl implements TaskColumnService {

  private final TaskColumnRepository repository;
  private final TaskColumnMapper mapper;
  private final ProjectAuthorizationService projectAuthorizationService;

  @Override
  public void createDefaultColumns(String projectId) {

    repository.saveAll(List.of(

        TaskColumn.builder()
            .projectId(projectId)
            .name("Todo")
            .position(1)
            .build(),

        TaskColumn.builder()
            .projectId(projectId)
            .name("In Progress")
            .position(2)
            .build(),

        TaskColumn.builder()
            .projectId(projectId)
            .name("Done")
            .position(3)
            .build()

    ));

  }

  @Override
  public List<TaskColumnResponse> getColumns(
      String organizationId,
      String projectId,
      String userId) {

    projectAuthorizationService.requireProjectAccess(
        organizationId,
        projectId,
        userId);

    return repository
        .findByProjectIdOrderByPositionAsc(projectId)
        .stream()
        .map(mapper::toResponse)
        .toList();

  }

}
