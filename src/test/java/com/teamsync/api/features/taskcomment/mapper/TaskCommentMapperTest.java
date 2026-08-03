package com.teamsync.api.features.taskcomment.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.teamsync.api.features.taskcomment.dto.request.CreateCommentRequest;
import com.teamsync.api.features.taskcomment.dto.response.CommentResponse;
import com.teamsync.api.features.taskcomment.entity.TaskComment;

class TaskCommentMapperTest {

    private static final String TASK_ID = "task-1";
    private static final String COMMENT_ID = "comment-1";
    private static final String USER_ID = "user-1";

    private final TaskCommentMapper mapper = new TaskCommentMapper();

    @Test
    void shouldMapToEntity() {

        // Arrange
        CreateCommentRequest request =
                new CreateCommentRequest("This is a comment.");

        // Act
        TaskComment comment = mapper.toEntity(
                request,
                TASK_ID,
                USER_ID);

        // Assert
        assertAll(
                () -> assertEquals(TASK_ID, comment.getTaskId()),
                () -> assertEquals(USER_ID, comment.getUserId()),
                () -> assertEquals("This is a comment.", comment.getContent()),
                () -> assertFalse(comment.isEdited())
        );

    }

    @Test
    void shouldMapToResponse() {

        // Arrange
        Instant createdAt = Instant.now();
        Instant updatedAt = createdAt.plusSeconds(60);

        TaskComment comment = TaskComment.builder()
                .taskId(TASK_ID)
                .userId(USER_ID)
                .content("This is a comment.")
                .edited(true)
                .build();

        comment.setId(COMMENT_ID);
        comment.setCreatedAt(createdAt);
        comment.setUpdatedAt(updatedAt);

        // Act
        CommentResponse response = mapper.toResponse(comment);

        // Assert
        assertAll(
                () -> assertEquals(COMMENT_ID, response.id()),
                () -> assertEquals(TASK_ID, response.taskId()),
                () -> assertEquals(USER_ID, response.userId()),
                () -> assertEquals("This is a comment.", response.content()),
                () -> assertEquals(true, response.edited()),
                () -> assertEquals(createdAt, response.createdAt()),
                () -> assertEquals(updatedAt, response.updatedAt())
        );

    }

}
