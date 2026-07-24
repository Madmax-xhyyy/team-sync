package com.teamsync.api.features.board.dto;

import java.util.List;

public record BoardColumnResponse(

        String id,

        String name,

        Integer position,

        List<BoardTaskResponse> tasks

) {}
