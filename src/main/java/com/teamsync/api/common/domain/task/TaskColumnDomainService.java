package com.teamsync.api.common.domain.task;

import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.task.entity.TaskColumn;
import com.teamsync.api.features.task.repository.TaskColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskColumnDomainService {

    private final TaskColumnRepository taskColumnRepository;

    public TaskColumn getById(String columnId) {

        return taskColumnRepository.findById(columnId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task column not found."
                        )
                );
    }

}