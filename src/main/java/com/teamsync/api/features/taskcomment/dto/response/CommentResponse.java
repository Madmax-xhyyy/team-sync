package com.teamsync.api.features.taskcomment.dto.response;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentResponse(

    @Schema(description = "Comment ID", example = "1") String id,

    @Schema(description = "Task ID", example = "1") String taskId,

    @Schema(description = "User ID", example = "1") String userId,

    @Schema(description = "Comment content", example = "Comment content") String content,

    @Schema(description = "Comment edited", example = "true") boolean edited,

    @Schema(description = "Comment created at", example = "2022-01-01T00:00:00Z") Instant createdAt,

    @Schema(description = "Comment updated at", example = "2022-01-01T00:00:00Z") Instant updatedAt

) {
}