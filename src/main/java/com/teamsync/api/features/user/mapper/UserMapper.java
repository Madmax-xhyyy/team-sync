package com.teamsync.api.features.user.mapper;

import com.teamsync.api.features.user.dto.response.UserProfileResponse;
import com.teamsync.api.features.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileResponse toProfileResponse(User user) {

        return new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name()
        );

    }

}
