package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.*;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import com.deloitte.ddwatch.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private QualityReportService qualityReportService;
    @Autowired
    private TagRepository tagRepository;


    @Transactional
    public Project create(Project project) {
        if (!ObjectUtils.isEmpty(project.getTags())) {
            for (Tag tag : project.getTags()) {

                Optional<Tag> persistedTag = tagRepository.findByName(tag.getName());
                if(persistedTag.isPresent()) {
                    persistedTag.get().getProjects().add(project);
                    project.getTags().remove(tag);
                    project.addTag(persistedTag.get());

                } else {
                    project.addTag(tag);
                }
//                tag.getProjects().add(project);
//                Tag tag = tagService.safelyGetByName(tagName);

//                project.addTag(tag);
            }
        }
        project = projectRepository.save(project);
        projectRepository.save(project);
        return project;
    }


    public Project findById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new RuntimeException("No such project found");
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

        qualityReport = qualityReportService.addReport(project.getSonarQubeUrl(), project.getSonarComponentKey(), qualityReport);
        project.addQualityReport(qualityReport);
        project.setLastQualityReport(qualityReport.getUpdateDate());
        return project;
    }

    @Transactional
    public Project addReport(long id, InputStream inputStream, QualityReport qualityReport) throws IOException {
        Project project = findById(id);

        qualityReport = qualityReportService.addReport(inputStream, qualityReport);
        project.addQualityReport(qualityReport);
        project.setLastQualityReport(qualityReport.getUpdateDate());
        return project;
    }
}
