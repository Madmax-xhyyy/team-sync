package com.teamsync.api.features.organization.controller;

import com.teamsync.api.common.response.ApiResponse;
import com.teamsync.api.features.auth.security.userdetails.CustomUserDetails;
import com.teamsync.api.features.organization.dto.request.CreateOrganizationRequest;
import com.teamsync.api.features.organization.dto.response.OrganizationResponse;
import com.teamsync.api.features.organization.service.OrganizationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Tag(name = "Organizations", description = "Endpoints for managing organizations.")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

  private final OrganizationService organizationService;

  @Operation(summary = "Create organization", description = "Creates a new organization and assigns the authenticated user as the owner.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Organization created successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized.")
  })
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

  @Operation(summary = "Get my organizations", description = "Returns all organizations where the authenticated user is a member.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organizations retrieved successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized.")
  })
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

  @Operation(summary = "Get organization", description = "Returns an organization by its ID if the authenticated user is a member.")
  @ApiResponses({
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Organization retrieved successfully."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden."),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Organization not found.")
  })
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