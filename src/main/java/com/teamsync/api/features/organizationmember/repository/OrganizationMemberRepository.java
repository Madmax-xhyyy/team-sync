package com.teamsync.api.features.organizationmember.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamsync.api.features.organizationmember.entity.OrganizationMember;

public interface OrganizationMemberRepository
                extends MongoRepository<OrganizationMember, String> {
        List<OrganizationMember> findByUserId(String userId);

        Optional<OrganizationMember> findByOrganizationIdAndUserId(String organizationId, String userId);

        List<OrganizationMember> findByOrganizationId(String organizationId);

        Optional<OrganizationMember> findByIdAndOrganizationId(
        String memberId,
        String organizationId
);
}
