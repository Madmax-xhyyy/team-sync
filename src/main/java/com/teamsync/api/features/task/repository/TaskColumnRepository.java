package com.teamsync.api.features.task.repository;

import com.teamsync.api.features.task.entity.TaskColumn;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskColumnRepository
    extends MongoRepository<TaskColumn, String> {

  List<TaskColumn> findByProjectIdOrderByPositionAsc(String projectId);

}
