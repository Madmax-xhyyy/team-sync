package com.teamsync.api.features.taskcomment.entity;

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

@Document(collection = "task_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CompoundIndexes({

        @CompoundIndex(name = "task_created_idx", def = "{'task_id': 1, 'created_at': 1}")

})
public class TaskComment extends AuditableEntity {

    @Indexed
    @Field("task_id")
    private String taskId;

    @Indexed
    @Field("user_id")
    private String userId;

    private String content;

    private boolean edited;

}