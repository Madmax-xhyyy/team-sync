package com.teamsync.api.features.user.service;

import com.teamsync.api.features.user.dto.request.UpdateProfileRequest;
import com.teamsync.api.features.user.dto.response.UserProfileResponse;

public interface UserService {

    UserProfileResponse getCurrentUser(String userId);

    UserProfileResponse updateCurrentUser(
            String userId,
            UpdateProfileRequest request
    );

}
