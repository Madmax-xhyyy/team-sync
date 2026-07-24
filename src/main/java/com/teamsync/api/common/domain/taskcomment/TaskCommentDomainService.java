package com.teamsync.api.common.domain.taskcomment;

import com.teamsync.api.common.exception.NotFoundException;
import com.teamsync.api.features.taskcomment.entity.TaskComment;
import com.teamsync.api.features.taskcomment.repository.TaskCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskCommentDomainService {

    private final TaskCommentRepository repository;

    public TaskComment getById(String commentId) {

        return repository.findById(commentId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Comment not found."
                        )
                );
    }

}
