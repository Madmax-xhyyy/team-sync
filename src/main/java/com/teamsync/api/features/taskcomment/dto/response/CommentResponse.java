package com.teamsync.api.features.taskcomment.dto.response;

import java.time.Instant;

public record CommentResponse(

  String id,

  String taskId,

  String userId,

  String content,

  boolean edited,

  Instant createdAt,

  Instant updatedAt

) {}