package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.ProjectRepo;
import com.deloitte.ddwatch.repositories.ProjectRepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProjectRepoService {

    @Autowired
    ProjectRepoRepository projectRepoRepository;

    @Autowired
    QualityReportService qualityReportService;

    @Autowired
    ProjectService projectService;

    @Transactional
    public Optional<ProjectRepo>  getProjectRepo(Long repoId) {
        return projectRepoRepository.findById(repoId);
    }

}
