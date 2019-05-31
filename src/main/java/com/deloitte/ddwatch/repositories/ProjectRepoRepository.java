package com.deloitte.ddwatch.repositories;

import com.deloitte.ddwatch.model.ProjectRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepoRepository extends JpaRepository<ProjectRepo, Long> {
}
