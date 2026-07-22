package com.teamsync.api.features.organizationmember.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.teamsync.api.features.organization.entity.OrganizationRole;

import java.time.Instant;

@Document(collection = "organization_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationMember {

  @Id
  private String id;

  private String organizationId;

  private String userId;

  private OrganizationRole role;

  private Instant joinedAt;

}