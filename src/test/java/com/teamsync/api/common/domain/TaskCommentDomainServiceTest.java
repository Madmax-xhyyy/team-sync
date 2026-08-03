package com.teamsync.api.common.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.teamsync.api.common.domain.taskcomment.TaskCommentDomainService;
import com.teamsync.api.common.exception.NotFoundException;
import com.teamsync.api.features.taskcomment.entity.TaskComment;
import com.teamsync.api.features.taskcomment.repository.TaskCommentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskCommentDomainServiceTest {

    private static final String COMMENT_ID = "comment-1";

    @Mock
    private TaskCommentRepository repository;

    @InjectMocks
    private TaskCommentDomainService service;

    private TaskComment createComment() {

        TaskComment comment = TaskComment.builder()
                .taskId("task-1")
                .userId("user-1")
                .content("First comment")
                .edited(false)
                .build();

        comment.setId(COMMENT_ID);

        return comment;
    }

    @Test
    void shouldReturnCommentWhenCommentExists() {

        // Arrange
        TaskComment comment = createComment();

        when(repository.findById(COMMENT_ID))
                .thenReturn(Optional.of(comment));

        // Act
        TaskComment result = service.getById(COMMENT_ID);

        // Assert
        assertNotNull(result);
        assertEquals(COMMENT_ID, result.getId());

        verify(repository)
                .findById(COMMENT_ID);

    }

    @Test
    void shouldThrowWhenCommentDoesNotExist() {

        // Arrange
        when(repository.findById(COMMENT_ID))
                .thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> service.getById(COMMENT_ID));

        // Assert
        assertEquals(
                "Comment not found.",
                exception.getMessage());

        verify(repository)
                .findById(COMMENT_ID);

    }

}
