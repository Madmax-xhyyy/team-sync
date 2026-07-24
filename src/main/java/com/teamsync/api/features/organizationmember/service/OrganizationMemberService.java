package com.teamsync.api.features.organizationmember.service;

import com.teamsync.api.features.organizationmember.dto.request.AddMemberRequest;
import com.teamsync.api.features.organizationmember.dto.request.UpdateMemberRoleRequest;
import com.teamsync.api.features.organizationmember.dto.response.MemberResponse;

import java.util.List;

public interface OrganizationMemberService {

    MemberResponse addMember(
            String organizationId,
            String currentUserId,
            AddMemberRequest request
    );

    List<MemberResponse> getMembers(
            String organizationId,
            String currentUserId
    );

    MemberResponse updateRole(
            String organizationId,
            String memberId,
            String currentUserId,
            UpdateMemberRoleRequest request
    );

    void removeMember(
            String organizationId,
            String memberId,
            String currentUserId
    );

}
