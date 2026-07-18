package com.teamsync.api.features.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @NotBlank(message = "First name is required.")
        @Size(max = 50)
        String firstName,

        @NotBlank(message = "Last name is required.")
        @Size(max = 50)
        String lastName

) {}
