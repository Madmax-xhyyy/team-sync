package com.teamsync.api.features.organization.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.organization.dto.request.CreateOrganizationRequest;
import com.teamsync.api.features.organization.dto.response.OrganizationResponse;
import com.teamsync.api.features.organization.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

  private final OrganizationService organizationService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<OrganizationResponse> createOrganization(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @Valid @RequestBody CreateOrganizationRequest request) {

    OrganizationResponse response = organizationService.createOrganization(
        currentUser.getUserId(),
        request);

    return ApiResponse.<OrganizationResponse>builder()
        .success(true)
        .message("Organization created successfully.")
        .data(response)
        .build();
  }

  @GetMapping("/my")
  public ApiResponse<List<OrganizationResponse>> getMyOrganizations(
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    List<OrganizationResponse> response = organizationService.getMyOrganizations(
        currentUser.getUserId());

    return ApiResponse.<List<OrganizationResponse>>builder()
        .success(true)
        .message("Organizations retrieved successfully.")
        .data(response)
        .build();
  }

  @GetMapping("/{organizationId}")
  public ApiResponse<OrganizationResponse> getOrganization(
      @PathVariable String organizationId,
      @AuthenticationPrincipal CustomUserDetails currentUser) {

    OrganizationResponse response = organizationService.getOrganization(
        organizationId,
        currentUser.getUserId());

    return ApiResponse.<OrganizationResponse>builder()
        .success(true)
        .message("Organization retrieved successfully.")
        .data(response)
        .build();
  }

}