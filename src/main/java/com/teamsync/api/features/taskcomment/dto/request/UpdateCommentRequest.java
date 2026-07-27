package com.teamsync.api.features.taskcomment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateCommentRequest(

    @Schema(description = "Comment content", example = "Updated comment") @NotBlank @Size(max = 5000) String content

) {
}
