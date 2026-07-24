package com.teamsync.api.common.security;

import com.teamsync.api.common.domain.task.TaskDomainService;
import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.features.task.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskAuthorizationService {

    private final TaskColumnAuthorizationService taskColumnAuthorizationService;
    private final TaskDomainService taskDomainService;

    public Task requireTaskMember(
            String organizationId,
            String projectId,
            String columnId,
            String taskId,
            String userId
    ) {

        taskColumnAuthorizationService.requireTaskColumnAccess(
                organizationId,
                projectId,
                columnId,
                userId
        );

        Task task = taskDomainService.getById(taskId);

        if (!task.getColumnId().equals(columnId)) {

            throw new ForbiddenException(
                    "Task does not belong to this column."
            );

        }

        return task;

    }

}
