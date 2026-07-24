package com.teamsync.api.features.organizationmember.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.organizationmember.dto.request.AddMemberRequest;
import com.teamsync.api.features.organizationmember.dto.request.UpdateMemberRoleRequest;
import com.teamsync.api.features.organizationmember.dto.response.MemberResponse;
import com.teamsync.api.features.organizationmember.service.OrganizationMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations/{organizationId}/members")
public class OrganizationMemberController {

    private final OrganizationMemberService organizationMemberService;

    @PostMapping
    public ApiResponse<MemberResponse> addMember(
            @PathVariable String organizationId,
            @Valid @RequestBody AddMemberRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        MemberResponse response =
                organizationMemberService.addMember(
                        organizationId,
                        currentUser.getUserId(),
                        request
                );

        return ApiResponse.<MemberResponse>builder()
                .success(true)
                .message("Member added successfully.")
                .data(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<MemberResponse>> getMembers(
            @PathVariable String organizationId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

    List<MemberResponse> response =
            organizationMemberService.getMembers(
                    organizationId,
                    currentUser.getUserId()
            );

    return ApiResponse.<List<MemberResponse>>builder()
            .success(true)
            .message("Members retrieved successfully.")
            .data(response)
            .build();
    }

    @PatchMapping("/{memberId}/role")
    public ApiResponse<MemberResponse> updateRole(
            @PathVariable String organizationId,
            @PathVariable String memberId,
            @Valid @RequestBody UpdateMemberRoleRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

    MemberResponse response =
            organizationMemberService.updateRole(
                    organizationId,
                    memberId,
                    currentUser.getUserId(),
                    request
            );

    return ApiResponse.<MemberResponse>builder()
            .success(true)
            .message("Member role updated successfully.")
            .data(response)
            .build();
    }

    @DeleteMapping("/{memberId}")
public ApiResponse<Void> removeMember(
        @PathVariable String organizationId,
        @PathVariable String memberId,
        @AuthenticationPrincipal CustomUserDetails currentUser
) {

    organizationMemberService.removeMember(
            organizationId,
            memberId,
            currentUser.getUserId()
    );

    return ApiResponse.<Void>builder()
            .success(true)
            .message("Member removed successfully.")
            .build();
}

}