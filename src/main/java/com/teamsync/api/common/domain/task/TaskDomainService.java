package com.teamsync.api.common.domain.task;

import com.teamsync.api.common.exception.ResourceNotFoundException;
import com.teamsync.api.features.task.entity.Task;
import com.teamsync.api.features.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskDomainService {

    private final TaskRepository taskRepository;

    public Task getById(String taskId) {

      return taskRepository.findById(taskId)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Task not found."
                    )
            );
    }

}