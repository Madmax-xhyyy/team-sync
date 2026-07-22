package com.teamsync.api.features.user.entity;

import com.teamsync.api.common.constants.AuthProvider;
import com.teamsync.api.common.constants.Role;
import com.teamsync.api.common.domain.AuditableEntity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User extends AuditableEntity {

    private String firstName;

    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String avatarUrl;

    @Builder.Default
    private Role role = Role.ROLE_USER;

    @Builder.Default
    private AuthProvider provider = AuthProvider.LOCAL;

    private String providerId;

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private boolean emailVerified = false;

    private Instant lastLoginAt;

}
