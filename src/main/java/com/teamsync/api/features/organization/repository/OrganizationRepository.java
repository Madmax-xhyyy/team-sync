package com.teamsync.api.features.organization.repository;

import com.teamsync.api.features.organization.entity.Organization;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrganizationRepository extends MongoRepository<Organization, String> {

  List<Organization> findByIdIn(List<String> ids);

}