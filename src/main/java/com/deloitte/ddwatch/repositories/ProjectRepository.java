package com.deloitte.ddwatch.repositories;

import com.deloitte.ddwatch.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
