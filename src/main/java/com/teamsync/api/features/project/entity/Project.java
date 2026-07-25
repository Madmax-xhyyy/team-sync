package com.teamsync.api.features.project.entity;

import com.teamsync.api.common.domain.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({

    @CompoundIndex(name = "organization_name_idx", def = "{'organization_id': 1, 'name': 1}", unique = true)

})
public class Project extends AuditableEntity {

  @Indexed
  @Field("organization_id")
  private String organizationId;

  private String name;

  private String description;

  @Indexed
  @Field("created_by")
  private String createdBy;

}