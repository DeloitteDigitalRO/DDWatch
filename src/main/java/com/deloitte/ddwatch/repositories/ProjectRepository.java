package com.deloitte.ddwatch.repositories;

import com.deloitte.ddwatch.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
}
