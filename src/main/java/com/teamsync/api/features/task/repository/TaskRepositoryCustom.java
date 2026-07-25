package com.teamsync.api.features.task.repository;

import com.teamsync.api.features.task.dto.request.TaskFilter;
import com.teamsync.api.features.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepositoryCustom {

  Page<Task> searchTasks(
      String projectId,
      String keyword,
      TaskFilter filter,
      Pageable pageable);

}
