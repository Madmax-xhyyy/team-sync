package com.teamsync.api.features.organization.repository;

import com.teamsync.api.features.organization.entity.OrganizationMember;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrganizationMemberRepository
        extends MongoRepository<OrganizationMember, String> {
    List<OrganizationMember> findByUserId(String userId);

    Optional<OrganizationMember> findByOrganizationIdAndUserId(
            String organizationId,
            String userId);
}
