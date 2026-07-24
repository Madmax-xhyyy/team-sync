package com.teamsync.api.common.security;

import com.teamsync.api.common.domain.taskcomment.TaskCommentDomainService;
import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.features.taskcomment.entity.TaskComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskCommentAuthorizationService {

    private final TaskAuthorizationService taskAuthorizationService;
    private final TaskCommentDomainService taskCommentDomainService;

    public TaskComment requireCommentAccess(
            String organizationId,
            String projectId,
            String columnId,
            String taskId,
            String commentId,
            String userId
    ) {

        taskAuthorizationService.requireTaskAccess(
                organizationId,
                projectId,
                columnId,
                taskId,
                userId
        );

        TaskComment comment =
                taskCommentDomainService.getById(commentId);

        if (!comment.getTaskId().equals(taskId)) {
            throw new ForbiddenException(
                    "Comment does not belong to this task."
            );
        }

        return comment;
    }

}
