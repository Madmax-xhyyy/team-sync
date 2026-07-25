package com.teamsync.api.features.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamsync.api.features.project.entity.Project;

public interface ProjectRepository
    extends MongoRepository<Project, String> {

  Page<Project> findByOrganizationId(String organizationId, Pageable pageable);

  Page<Project> findByOrganizationIdAndNameContainingIgnoreCase(
      String organizationId,
      String name,
      Pageable pageable);

}
