package com.teamsync.api.features.taskcomment.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.teamsync.api.common.domain.AuditableEntity;

import lombok.*;

@Document(collection = "task_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskComment extends AuditableEntity {

    @Field("task_id")
    private String taskId;

    @Field("user_id")
    private String userId;

    private String content;

    private boolean edited;

}
