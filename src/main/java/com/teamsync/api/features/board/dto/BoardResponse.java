package com.teamsync.api.features.board.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record BoardResponse(

    @Schema(description = "Project ID", example = "1") String projectId,

    @Schema(description = "Project name", example = "Project Alpha") String projectName,

    @Schema(description = "List of columns") List<BoardColumnResponse> columns

) {
}
