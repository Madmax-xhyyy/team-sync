package com.teamsync.api.common.security;

import com.teamsync.api.common.exception.ForbiddenException;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.organizationmember.repository.OrganizationMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationAuthorizationService {

    private final OrganizationMemberRepository organizationMemberRepository;

    public OrganizationMember requireOrganizationAccess(
            String organizationId,
            String userId
    ) {

        return organizationMemberRepository
                .findByOrganizationIdAndUserId(
                        organizationId,
                        userId
                )
                .orElseThrow(() ->
                        new ForbiddenException(
                                "You are not a member of this organization."
                        )
                );

    }

}