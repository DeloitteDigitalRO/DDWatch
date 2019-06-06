package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.*;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class.getCanonicalName());

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private QualityReportService qualityReportService;

    @Autowired
    private DeliveryReportService deliveryReportService;


    @Transactional
    public Project create(final Project project, Set<Tag> tags) {
        tagService.setTags(project, tags);
        project.getProjectRepos().forEach(repo -> repo.setProject(project));
        return projectRepository.save(project);
    }

    @Transactional
    public Project create(Project project) {
        return create(project, new TreeSet<>());
    }


    public Project findById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new RuntimeException("No such project found");
        }
        return project.get();
    }

    public Optional<Project> findProject(Long id) {
        return projectRepository.findById(id);
    }


    public Set<Project> findByTag(String tagName) {
        Tag tag = tagService.safelyGetByName(tagName);
        Set<Project> projects = tag.getProjects();
        return projects;
    }

    public List<Project> findAll() {
        List<Project> projects = projectRepository.findAll();
        return projects;
    }

    @Transactional
    public Project addProjectRepo(ProjectRepo projectRepo) {
        Project project = findById(projectRepo.getProject().getId());
        project.addProjectRepo(projectRepo);
        project.setLastQualityReport(projectRepo.getIsDefault() ? LocalDateTime.now() : project.getLastQualityReport());
        projectRepository.save(project);
        return project;
    }

    @Transactional
    public void removeProject(Project project) {
        projectRepository.delete(project);
    }
}
