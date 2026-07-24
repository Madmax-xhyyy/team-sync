package com.teamsync.api.features.organizationmember.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.organizationmember.dto.request.AddMemberRequest;
import com.teamsync.api.features.organizationmember.dto.response.MemberResponse;
import com.teamsync.api.features.organizationmember.service.OrganizationMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}