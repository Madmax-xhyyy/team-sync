package com.teamsync.api.features.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

}