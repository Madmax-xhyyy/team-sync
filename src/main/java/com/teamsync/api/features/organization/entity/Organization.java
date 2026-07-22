package com.teamsync.api.features.organization.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import com.teamsync.api.common.domain.AuditableEntity;

@Document(collection = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization extends AuditableEntity {

    private String name;

    private String description;

}
