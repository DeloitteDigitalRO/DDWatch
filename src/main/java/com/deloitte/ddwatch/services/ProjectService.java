package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.exceptions.NotFoundException;
import com.deloitte.ddwatch.model.*;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
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
    public Project create(Project project, Set<Tag> tags) {
        tagService.setTags(project, tags);
        project = projectRepository.save(project);
        return project;
    }

    @Transactional
    public Project create(Project project) {
        return create(project, new TreeSet<>());
    }


    public Project findById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new NotFoundException("No such project found");
        }
        return project.get();
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
    public Project addReport(long id, QualityReport qualityReport) {
        Project project = findById(id);

        qualityReport = qualityReportService.create(project.getSonarQubeUrl(), project.getSonarComponentKey(), qualityReport);
        project.addQualityReport(qualityReport);
        project.setQualityStatus(qualityReport.getQualityStatus());
        project.setLastQualityReport(qualityReport.getUpdateDate());
        return project;
    }

    @Transactional
    public Project addReport(long id, InputStream inputStream, QualityReport qualityReport) throws IOException {
        Project project = findById(id);

        qualityReport = qualityReportService.create(inputStream, qualityReport);
        project.addQualityReport(qualityReport);
        project.setLastQualityReport(qualityReport.getUpdateDate());
        project.setQualityStatus(qualityReport.getQualityStatus());
        return project;
    }

    @Transactional
    public Project addDeliveryReport(long id, DeliveryReport deliveryReport) {
        Project project = findById(id);

        deliveryReport = deliveryReportService.create(deliveryReport);
        project.addDeliveryReport(deliveryReport);
        project.setLastDeliveryReport(deliveryReport.getUpdateDate());
        return project;
    }

    @Transactional
    public void removeProject(Project project) {
        projectRepository.delete(project);
    }
}
