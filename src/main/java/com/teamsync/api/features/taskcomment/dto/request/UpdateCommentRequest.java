package com.teamsync.api.features.taskcomment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCommentRequest(

        @NotBlank
        @Size(max = 5000)
        String content

) {}
