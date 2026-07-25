package com.teamsync.api.features.task.entity;

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

import java.time.Instant;

@Document(collection = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({

        @CompoundIndex(name = "column_position_idx", def = "{'column_id': 1, 'position': 1}")

})
public class Task extends AuditableEntity {

    @Indexed
    @Field("project_id")
    private String projectId;

    @Indexed
    @Field("column_id")
    private String columnId;

    private String title;

    private String description;

    private TaskPriority priority;

    private TaskType type;

    @Indexed
    @Field("assignee_id")
    private String assigneeId;

    @Indexed
    @Field("reporter_id")
    private String reporterId;

    private Instant dueDate;

    /**
     * Used for ordering tasks within a column.
     */
    private Integer position;

}