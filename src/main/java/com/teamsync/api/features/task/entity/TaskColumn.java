package com.teamsync.api.features.task.entity;

import com.teamsync.api.common.domain.AuditableEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "task_columns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskColumn extends AuditableEntity {

  private String projectId;

  private String name;

  /**
   * Used to order columns on the Kanban board.
   */
  private Integer position;

}
