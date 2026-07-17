package com.teamsync.api.features.health.controller;

import com.teamsync.api.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<String> health() {

        return ApiResponse.<String>builder()
                .success(true)
                .message("TeamSync API is running.")
                .data("Healthy")
                .build();

    }

}