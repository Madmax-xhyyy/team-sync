package com.teamsync.api.features.taskcomment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateCommentRequest(

    @Schema(description = "Task ID", example = "1") @NotBlank @Size(max = 5000) String content

) {
}
