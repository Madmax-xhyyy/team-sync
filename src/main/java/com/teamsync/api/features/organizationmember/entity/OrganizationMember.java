package com.teamsync.api.features.organizationmember.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import com.teamsync.api.common.domain.AuditableEntity;
import com.teamsync.api.features.organization.entity.OrganizationRole;

import java.time.Instant;

@Document(collection = "organization_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationMember extends AuditableEntity {

  private String organizationId;

  private String userId;

  private OrganizationRole role;

  private Instant joinedAt;

  public boolean isOwner() {
    return role == OrganizationRole.OWNER;
  }

  public boolean isAdmin() {
      return role == OrganizationRole.ADMIN;
  }

  public boolean isMember() {
      return role == OrganizationRole.MEMBER;
  }

  public boolean isGuest() {
      return role == OrganizationRole.GUEST;
  }

}