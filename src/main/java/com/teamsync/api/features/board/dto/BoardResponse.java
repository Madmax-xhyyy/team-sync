package com.teamsync.api.features.board.dto;

import java.util.List;

public record BoardResponse(

        String projectId,

        String projectName,

        List<BoardColumnResponse> columns

) {}
