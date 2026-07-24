package com.teamsync.api.features.task.entity;

import com.teamsync.api.common.domain.AuditableEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task extends AuditableEntity {

    private String projectId;

    private String columnId;

    private String title;

    private String description;

    private TaskPriority priority;

    private String assigneeId;

    private String reporterId;

    private Instant dueDate;

    /**
     * Used for ordering tasks within a column.
     */
    private Integer position;

}
