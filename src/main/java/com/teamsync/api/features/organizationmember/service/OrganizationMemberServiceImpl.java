package com.teamsync.api.features.organizationmember.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.teamsync.api.common.exception.BadRequestException;
import com.teamsync.api.common.exception.NotFoundException;
import com.teamsync.api.common.security.OrganizationAuthorizationService;
import com.teamsync.api.common.security.PermissionService;
import com.teamsync.api.features.organizationmember.dto.request.AddMemberRequest;
import com.teamsync.api.features.organizationmember.dto.request.UpdateMemberRoleRequest;
import com.teamsync.api.features.organizationmember.dto.response.MemberResponse;
import com.teamsync.api.features.organizationmember.entity.OrganizationMember;
import com.teamsync.api.features.organizationmember.mapper.OrganizationMemberMapper;
import com.teamsync.api.features.organizationmember.repository.OrganizationMemberRepository;
import com.teamsync.api.features.user.entity.User;
import com.teamsync.api.features.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationMemberServiceImpl
        implements OrganizationMemberService {

    private final UserRepository userRepository;

    private final OrganizationMemberRepository organizationMemberRepository;

    private final OrganizationAuthorizationService organizationAuthorizationService;

    private final PermissionService permissionService;

    private final OrganizationMemberMapper organizationMemberMapper;

     @Override
    public MemberResponse addMember(
            String organizationId,
            String currentUserId,
            AddMemberRequest request
    ) {

        // Verify requester belongs to the organization
        OrganizationMember currentMember =
                organizationAuthorizationService.requireOrganizationAccess(
                        organizationId,
                        currentUserId
                );

        // Verify requester has permission
        permissionService.requireMemberManagementPermission(currentMember);

        // Find the invited user
        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("User not found."));

        // Prevent duplicate membership
        if (organizationMemberRepository
                .findByOrganizationIdAndUserId(
                        organizationId,
                        user.getId()
                )
                .isPresent()) {

            throw new BadRequestException(
                    "User is already a member of this organization."
            );
        }

        // Create membership
        OrganizationMember member =
                OrganizationMember.builder()
                        .organizationId(organizationId)
                        .userId(user.getId())
                        .role(request.role())
                        .build();

        // Save
        OrganizationMember savedMember =
                organizationMemberRepository.save(member);

        // Return response
        return organizationMemberMapper.toResponse(
                savedMember,
                user
        );
    }

    @Override
public List<MemberResponse> getMembers(
        String organizationId,
        String currentUserId
) {

    organizationAuthorizationService.requireOrganizationAccess(
            organizationId,
            currentUserId
    );

    List<OrganizationMember> members =
            organizationMemberRepository.findByOrganizationId(
                    organizationId
            );

    List<String> userIds =
            members.stream()
                    .map(OrganizationMember::getUserId)
                    .toList();

    List<User> users =
            userRepository.findByIdIn(userIds);

    Map<String, User> userMap =
            users.stream()
                    .collect(Collectors.toMap(
                            User::getId,
                            user -> user
                    ));

    return members.stream()
            .map(member ->
                    organizationMemberMapper.toResponse(
                            member,
                            userMap.get(member.getUserId())
                    )
            )
            .toList();

}

    @Override
    public MemberResponse updateRole(
            String organizationId,
            String memberId,
            String currentUserId,
            UpdateMemberRoleRequest request
    ) {
        throw new UnsupportedOperationException(
                "Not implemented yet."
        );
    }

    @Override
    public void removeMember(
            String organizationId,
            String memberId,
            String currentUserId
    ) {
        throw new UnsupportedOperationException(
                "Not implemented yet."
        );
    }

}

