package com.teamsync.api.features.user.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.user.dto.request.UpdateProfileRequest;
import com.teamsync.api.features.user.dto.response.UserProfileResponse;
import com.teamsync.api.features.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        UserProfileResponse response =
                userService.getCurrentUser(currentUser.getUserId());

        return ApiResponse.<UserProfileResponse>builder()
                .success(true)
                .message("Profile retrieved successfully.")
                .data(response)
                .build();
    }

    @PutMapping("/me")
    public ApiResponse<UserProfileResponse> updateCurrentUser(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody UpdateProfileRequest request
    ) {

        UserProfileResponse response =
                userService.updateCurrentUser(
                        currentUser.getUserId(),
                        request
                );

        return ApiResponse.<UserProfileResponse>builder()
                .success(true)
                .message("Profile updated successfully.")
                .data(response)
                .build();
    }
}
