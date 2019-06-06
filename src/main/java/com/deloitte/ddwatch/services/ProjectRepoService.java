package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.ProjectRepo;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.model.Status;
import com.deloitte.ddwatch.repositories.ProjectRepoRepository;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProjectRepoService {

    @Autowired
    private ProjectRepoRepository projectRepoRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private QualityReportService qualityReportService;

    @Autowired
    private ProjectService projectService;

    @Transactional
    public Optional<ProjectRepo> getProjectRepo(Long repoId) {
        return projectRepoRepository.findById(repoId);
    }

    @Transactional
    public ProjectRepo getUpdatedProjectRepo(ProjectRepo projectRepo) {
        QualityReport qualityReport = getNewQualityReport(projectRepo);
        projectRepo = updateProjectRepo(projectRepo, qualityReport);
        return projectRepo;
    }

    @Transactional
    public ProjectRepo updateProjectRepo(ProjectRepo projectRepo, QualityReport qualityReport) {
        projectRepo.addQualityReport(qualityReport);
        projectRepoRepository.save(projectRepo);
        updateProject(projectRepo, qualityReport);
        return projectRepo;
    }


    public QualityReport getNewQualityReport(ProjectRepo projectRepo) {
        QualityReport qualityReport = qualityReportService.create(projectRepo.getSonarQubeUrl(), projectRepo.getSonarComponentKey());
        qualityReport.setProjectRepo(projectRepo);
        return qualityReport;
    }

    @Transactional
    public void updateProject(ProjectRepo projectRepo, QualityReport qualityReport) {
        Project project = projectRepo.getProject();

        if (projectRepo.getIsDefault()) {
            project.setQualityStatus(qualityReport.getQualityStatus());
            project.setLastQualityReport(qualityReport.getUpdateDate());
        }
        project.addProjectRepo(projectRepo);

        projectRepository.save(project);
    }

    @Transactional
    public ProjectRepo addQualityReport(ProjectRepo projectRepo, InputStream inputStream, QualityReport qualityReport) throws IOException {
        qualityReport = qualityReportService.create(inputStream, qualityReport);
        projectRepo.addQualityReport(qualityReport);
        qualityReport.setProjectRepo(projectRepo);
        projectRepoRepository.save(projectRepo);
        updateProject(projectRepo, qualityReport);
        return projectRepo;
    }


}
