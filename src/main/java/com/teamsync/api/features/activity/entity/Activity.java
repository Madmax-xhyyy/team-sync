package com.teamsync.api.features.activity.entity;

import com.teamsync.api.common.domain.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "activities")
@CompoundIndexes({

    @CompoundIndex(name = "organization_created_idx", def = "{'organization_id': 1, 'created_at': -1}"),

    @CompoundIndex(name = "project_created_idx", def = "{'project_id': 1, 'created_at': -1}"),

    @CompoundIndex(name = "task_created_idx", def = "{'task_id': 1, 'created_at': -1}")

})
public class Activity extends AuditableEntity {

  @Indexed
  @Field("organization_id")
  private String organizationId;

  @Indexed
  @Field("project_id")
  private String projectId;

  @Indexed
  @Field("task_id")
  private String taskId;

  @Indexed
  @Field("user_id")
  private String userId;

  @Field("entity_type")
  private ActivityEntityType entityType;

  @Field("action")
  private ActivityAction action;

  @Field("entity_id")
  private String entityId;

  @Field("description")
  private String description;

}
