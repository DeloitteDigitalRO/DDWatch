package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.model.*;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private QualityReportService qualityReportService;


    public Project create(ProjectDTO projectDTO) {
        Project project = modelMapper.map(projectDTO, Project.class);
        if (!ObjectUtils.isEmpty(project.getTags())) {
            for (Tag tag : project.getTags()) {


                tag.getProjects().add(project);
//                Tag tag = tagService.safelyGetByName(tagName);

//                project.addTag(tag);
            }
        }
        project = projectRepository.save(project);
        return project;
    }


    public Project safelyGet(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new RuntimeException("No such project found");
        }
        return project.get();
    }


    public ProjectDTO findById(Long id) {
        Project project = safelyGet(id);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        return projectDTO;
    }


    public List<ProjectDTO> findByTag(String tagName) {
        Tag tag = tagService.safelyGetByName(tagName);
        Set<Project> projects = tag.getProjects();

        List<ProjectDTO> projectDTOS = projects
                .stream()
                .map(p -> modelMapper.map(p, ProjectDTO.class))
                .collect(Collectors.toList());
        return projectDTOS;
    }


    public List<ProjectDTO> findAll() {
        List<Project> projects = projectRepository.findAll();

        List<ProjectDTO> projectDTOS = projects
                .stream()
                .map(p -> modelMapper.map(p, ProjectDTO.class))
                .collect(Collectors.toList());
        return projectDTOS;
    }

    @Transactional
    public Project addReport(long id, QualityReport qualityReport) {
        Project project = safelyGet(id);

        qualityReport = qualityReportService.addReport(project.getSonarQubeUrl(), project.getSonarComponentKey(), qualityReport);
        project.addQualityReport(qualityReport);
        project.setLastQualityReport(qualityReport.getUpdateDate());
        return project;
    }

    @Transactional
    public Project addReport(long id, InputStream inputStream, QualityReport qualityReport) throws IOException {
        Project project = safelyGet(id);

        qualityReport = qualityReportService.addReport(inputStream, qualityReport);
        project.addQualityReport(qualityReport);
        project.setLastQualityReport(qualityReport.getUpdateDate());
        return project;
    }
}
