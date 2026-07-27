package com.teamsync.api.features.organizationmember.controller;

import com.teamsync.api.common.pagination.PageQuery;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Organization Members", description = "Manage organization members")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations/{organizationId}/members")
public class OrganizationMemberController {

  private final OrganizationMemberService organizationMemberService;

  @Operation(summary = "Add member", description = "Adds a new member to an organization.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Member added successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
  })
  @PostMapping
  public ApiResponse<MemberResponse> addMember(
      @PathVariable String organizationId,
      @Valid @RequestBody AddMemberRequest request,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    MemberResponse response = organizationMemberService.addMember(
        organizationId,
        currentUser.getUserId(),
        request);

    return ApiResponse.<MemberResponse>builder()
        .success(true)
        .message("Member added successfully.")
        .data(response)
        .build();
  }

  @Operation(summary = "Get members", description = "Returns paginated members belonging to an organization.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Members retrieved successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
  })
  @GetMapping
  public ApiResponse<List<MemberResponse>> getMembers(
      @PathVariable String organizationId,
      @ModelAttribute PageQuery pageQuery,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    List<MemberResponse> response = organizationMemberService.getMembers(
        organizationId,
        currentUser.getUserId());

    return ApiResponse.<List<MemberResponse>>builder()
        .success(true)
        .message("Members retrieved successfully.")
        .data(response)
        .build();
  }

  @Operation(summary = "Update role", description = "Updates the role of a member in an organization.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Member role updated successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
  })
  @PatchMapping("/{memberId}/role")
  public ApiResponse<MemberResponse> updateRole(
      @PathVariable String organizationId,
      @PathVariable String memberId,
      @Valid @RequestBody UpdateMemberRoleRequest request,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    MemberResponse response = organizationMemberService.updateRole(
        organizationId,
        memberId,
        currentUser.getUserId(),
        request);

    return ApiResponse.<MemberResponse>builder()
        .success(true)
        .message("Member role updated successfully.")
        .data(response)
        .build();
  }

  @Operation(summary = "Remove member", description = "Removes a member from an organization.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Member removed successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden.")
  })
  @DeleteMapping("/{memberId}")
  public ApiResponse<Void> removeMember(
      @PathVariable String organizationId,
      @PathVariable String memberId,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    organizationMemberService.removeMember(
        organizationId,
        memberId,
        currentUser.getUserId());

    return ApiResponse.<Void>builder()
        .success(true)
        .message("Member removed successfully.")
        .build();
  }

}