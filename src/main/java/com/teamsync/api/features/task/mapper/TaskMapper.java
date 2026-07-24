package com.teamsync.api.features.task.mapper;

import com.teamsync.api.features.task.dto.request.CreateTaskRequest;
import com.teamsync.api.features.task.dto.response.TaskResponse;
import com.teamsync.api.features.task.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(
            CreateTaskRequest request,
            String projectId,
            String columnId,
            String reporterId,
            Integer position
    ) {

        return Task.builder()
                .projectId(projectId)
                .columnId(columnId)
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .type(request.type())
                .assigneeId(request.assigneeId())
                .reporterId(reporterId)
                .dueDate(request.dueDate())
                .position(position)
                .build();

    }

    public TaskResponse toResponse(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getProjectId(),
                task.getColumnId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getAssigneeId(),
                task.getReporterId(),
                task.getDueDate(),
                task.getPosition(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );

    }

}