package com.teamsync.api.features.organizationmember.mapper;

import com.teamsync.api.features.organizationmember.dto.response.MemberResponse;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMemberMapper {

    public MemberResponse toResponse(
            OrganizationMember member,
            User user
    ) {

        return new MemberResponse(
                member.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                member.getRole()
        );

    }

}
