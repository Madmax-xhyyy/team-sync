package com.teamsync.api.features.board.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record BoardColumnResponse(

    @Schema(description = "Column ID", example = "1") String id,

    @Schema(description = "Column name", example = "Todo") String name,

    @Schema(description = "Column position", example = "0") Integer position,

    @Schema(description = "List of tasks") List<BoardTaskResponse> tasks

) {
}
