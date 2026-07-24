package com.teamsync.api.features.board.service;

import com.teamsync.api.common.domain.project.ProjectDomainService;
import com.teamsync.api.common.security.ProjectAuthorizationService;
import com.teamsync.api.features.board.dto.BoardColumnResponse;
import com.teamsync.api.features.board.dto.BoardResponse;
import com.teamsync.api.features.board.dto.BoardTaskResponse;
import com.teamsync.api.features.board.mapper.BoardMapper;
import com.teamsync.api.features.project.entity.Project;
import com.teamsync.api.features.task.entity.Task;
import com.teamsync.api.features.task.repository.TaskRepository;
import com.teamsync.api.features.task.entity.TaskColumn;
import com.teamsync.api.features.task.repository.TaskColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final ProjectAuthorizationService projectAuthorizationService;
    private final ProjectDomainService projectDomainService;

    private final TaskColumnRepository taskColumnRepository;
    private final TaskRepository taskRepository;

    private final BoardMapper boardMapper;

    @Override
    public BoardResponse getBoard(
            String organizationId,
            String projectId,
            String userId
    ) {

        projectAuthorizationService.requireProjectAccess(
                organizationId,
                projectId,
                userId
        );

        Project project = projectDomainService.getById(projectId);

        List<TaskColumn> columns =
                taskColumnRepository
                        .findByProjectIdOrderByPositionAsc(projectId);

        List<Task> tasks =
                taskRepository.findByProjectId(projectId);

        Map<String, List<Task>> tasksByColumn =
                tasks.stream()
                        .collect(Collectors.groupingBy(Task::getColumnId));

        List<BoardColumnResponse> boardColumns =
                columns.stream()
                        .map(column -> {

                            List<BoardTaskResponse> boardTasks =
                                    tasksByColumn
                                            .getOrDefault(
                                                    column.getId(),
                                                    List.of()
                                            )
                                            .stream()
                                            .sorted(Comparator.comparing(Task::getPosition))
                                            .map(boardMapper::toTaskResponse)
                                            .toList();

                            return boardMapper.toColumnResponse(
                                    column,
                                    boardTasks
                            );

                        })
                        .toList();

        return boardMapper.toBoardResponse(
                project,
                boardColumns
        );

    }

}
