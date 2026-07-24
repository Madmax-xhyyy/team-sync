package com.teamsync.api.features.taskcomment.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamsync.api.features.taskcomment.entity.TaskComment;

public interface TaskCommentRepository extends MongoRepository<TaskComment, String> {

  List<TaskComment> findByTaskIdOrderByCreatedAtAsc(String taskId);
  
}
