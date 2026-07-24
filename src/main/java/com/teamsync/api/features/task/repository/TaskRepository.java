package com.teamsync.api.features.task.repository;

import com.teamsync.api.features.task.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByColumnIdOrderByPositionAsc(String columnId);

    long countByColumnId(String columnId);

    List<Task> findByProjectId(String projectId);

    boolean existsByColumnIdAndPosition(
        String columnId,
        Integer position
    );
}