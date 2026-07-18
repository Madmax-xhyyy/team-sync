package com.teamsync.api.features.organization.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    private String id;

    private String name;

    private String description;

    private String createdBy;

    private Instant createdAt;

    private Instant updatedAt;

}
