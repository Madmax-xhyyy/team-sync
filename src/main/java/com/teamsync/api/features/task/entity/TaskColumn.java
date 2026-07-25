package com.teamsync.api.features.task.entity;

import com.teamsync.api.common.domain.AuditableEntity;
import lombok.*;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "task_columns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({

    @CompoundIndex(name = "project_position_idx", def = "{'project_id': 1, 'position': 1}")

})
public class TaskColumn extends AuditableEntity {

  @Indexed
  @Field("project_id")
  private String projectId;

  private String name;

  /**
   * Used to order columns on the Kanban board.
   */
  private Integer position;

}
