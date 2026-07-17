package com.teamsync.api.common.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ApiError {

    private boolean success;

    private String message;

    private List<ValidationError> errors;

    @Builder.Default
    private Instant timestamp = Instant.now();

}