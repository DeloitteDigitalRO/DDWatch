package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.ProjectRepo;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.repositories.ProjectRepoRepository;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
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
    public ProjectRepo updateProjectRepo(long repoId) {
        ProjectRepo projectRepo = getProjectRepo(repoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectRepo not found"));
        QualityReport qualityReport = getNewQualityReport(projectRepo);
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
        Project project = projectService.findProject(projectRepo.getProject().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        project.setQualityStatus(projectRepo.getIsDefault() ? qualityReport.getQualityStatus() : project.getQualityStatus());
        project.addProjectRepo(projectRepo);
        project.setLastQualityReport(projectRepo.getIsDefault() ? qualityReport.getUpdateDate() : project.getLastQualityReport());

        projectRepository.save(project);
    }

    @Transactional
    public ProjectRepo addQualityReport(long repoId, InputStream inputStream, QualityReport qualityReport) throws IOException {
        ProjectRepo projectRepo = getProjectRepo(repoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectRepo not found"));

        qualityReport = qualityReportService.create(inputStream, qualityReport);
        projectRepo.addQualityReport(qualityReport);
        qualityReport.setProjectRepo(projectRepo);
        projectRepoRepository.save(projectRepo);
        updateProject(projectRepo, qualityReport);
        return projectRepo;
    }

}
