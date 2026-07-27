package com.teamsync.api.features.board.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.board.dto.BoardResponse;
import com.teamsync.api.features.board.service.BoardService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Board", description = "Operations related to the board")
@RestController
@RequestMapping("/api/v1/organizations/{organizationId}/projects/{projectId}/board")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  @GetMapping
  public ApiResponse<BoardResponse> getBoard(
      @PathVariable String organizationId,
      @PathVariable String projectId,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    BoardResponse response = boardService.getBoard(
        organizationId,
        projectId,
        currentUser.getUserId());

    return ApiResponse.<BoardResponse>builder()
        .success(true)
        .message("Board retrieved successfully.")
        .data(response)
        .build();

  }

}
