package com.teamsync.api.features.organizationmember.entity;

import lombok.*;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.teamsync.api.common.domain.AuditableEntity;
import com.teamsync.api.features.organization.entity.OrganizationRole;

import java.time.Instant;

@Document(collection = "organization_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeAlias("organizationMember")
@CompoundIndexes({

    @CompoundIndex(name = "organization_user_idx", def = "{'organization_id':1,'user_id':1}", unique = true)

})
public class OrganizationMember extends AuditableEntity {

  @Indexed
  @Field("organization_id")
  private String organizationId;

  @Indexed
  @Field("user_id")
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