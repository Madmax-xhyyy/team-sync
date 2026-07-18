package com.teamsync.api.features.user.dto.response;

public record UserProfileResponse(

        String id,

        String firstName,

        String lastName,

        String email,

        String role

) {}