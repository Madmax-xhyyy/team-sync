package com.teamsync.api.features.board.mapper;

import com.teamsync.api.features.board.dto.BoardColumnResponse;
import com.teamsync.api.features.board.dto.BoardResponse;
import com.teamsync.api.features.board.dto.BoardTaskResponse;
import com.teamsync.api.features.project.entity.Project;
import com.teamsync.api.features.task.entity.Task;
import com.teamsync.api.features.task.entity.TaskColumn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardMapper {

    public BoardTaskResponse toTaskResponse(Task task) {

        return new BoardTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getPriority(),
                task.getType(),
                task.getAssigneeId(),
                task.getPosition()
        );

    }

    public BoardColumnResponse toColumnResponse(
            TaskColumn column,
            List<BoardTaskResponse> tasks
    ) {

        return new BoardColumnResponse(
                column.getId(),
                column.getName(),
                column.getPosition(),
                tasks
        );

    }

    public BoardResponse toBoardResponse(
            Project project,
            List<BoardColumnResponse> columns
    ) {

        return new BoardResponse(
                project.getId(),
                project.getName(),
                columns
        );

    }

}