package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.ProjectDTO;
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
        Optional<Project> project = findProject(id);
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
        return tag.getProjects();
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project updateProject(Long id, ProjectDTO projectDTO){
        Project updateProject = projectRepository.getOne(id);
        if (projectDTO.getName() != null) {
            logger.info(String.format("Name was updated from: %s to %s for project with id:%d", updateProject.getName(), projectDTO.getName(), id));
            updateProject.setName(projectDTO.getName());
        }

        if(projectDTO.getDescription() != null){
            logger.info(String.format("Description was updated from: %s to %s for project with id:%d", updateProject.getDescription(), projectDTO.getDescription(), id));
            updateProject.setDescription(projectDTO.getDescription());
        }

        return projectRepository.save(updateProject);
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
