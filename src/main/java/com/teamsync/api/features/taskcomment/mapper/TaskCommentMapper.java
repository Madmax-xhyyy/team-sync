package com.teamsync.api.features.taskcomment.mapper;

import com.teamsync.api.features.taskcomment.dto.request.CreateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.response.CommentResponse;
import com.teamsync.api.features.taskcomment.entity.TaskComment;
import org.springframework.stereotype.Component;

@Component
public class TaskCommentMapper {

    public TaskComment toEntity(
            CreateCommentRequest request,
            String taskId,
            String userId
    ) {

        return TaskComment.builder()
                .taskId(taskId)
                .userId(userId)
                .content(request.content())
                .edited(false)
                .build();

    }

    public CommentResponse toResponse(TaskComment comment) {

        return new CommentResponse(
                comment.getId(),
                comment.getTaskId(),
                comment.getUserId(),
                comment.getContent(),
                comment.isEdited(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );

    }

}
