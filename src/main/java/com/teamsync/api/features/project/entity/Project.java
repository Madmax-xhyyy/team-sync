package com.teamsync.api.features.project.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import com.teamsync.api.common.domain.AuditableEntity;

@Document(collection = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends AuditableEntity {

  private String organizationId;

  private String name;

  private String description;

  private String createdBy;

}