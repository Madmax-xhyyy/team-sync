package com.teamsync.api.features.project.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.teamsync.api.features.project.entity.Project;

public interface ProjectRepository
    extends MongoRepository<Project, String> {

  List<Project> findByOrganizationId(String organizationId);

}
