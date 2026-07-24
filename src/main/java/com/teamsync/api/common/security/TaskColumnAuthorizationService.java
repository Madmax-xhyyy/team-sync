package com.teamsync.api.common.security;

import com.teamsync.api.common.domain.task.TaskColumnDomainService;
import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.features.task.entity.TaskColumn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskColumnAuthorizationService {

    private final ProjectAuthorizationService projectAuthorizationService;
    private final TaskColumnDomainService taskColumnDomainService;

    public TaskColumn requireTaskColumnAccess(
            String organizationId,
            String projectId,
            String columnId,
            String userId
    ) {

        projectAuthorizationService.requireProjectMember(
                organizationId,
                projectId,
                userId
        );

        TaskColumn column =
                taskColumnDomainService.getById(columnId);

        if (!column.getProjectId().equals(projectId)) {

            throw new ForbiddenException(
                    "Task column does not belong to this project."
            );

        }

        return column;

    }

}