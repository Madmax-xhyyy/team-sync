package com.teamsync.api.features.board.service;

import com.teamsync.api.features.board.dto.BoardResponse;

public interface BoardService {

    BoardResponse getBoard(
            String organizationId,
            String projectId,
            String userId
    );

}
