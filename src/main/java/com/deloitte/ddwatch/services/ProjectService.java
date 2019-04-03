package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.model.Tag;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectMock projectMock;
    @Autowired
    private QualityReportService qualityReportService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ModelMapper modelMapper;


    public Project create(ProjectDTO projectDTO) {
        ModelMapper modelMapper = new ModelMapper();

        Project project = modelMapper.map(projectDTO, Project.class);

        for(String tagName : projectDTO.getTagNames()) {
            Tag tag = tagService.safelyGetByName(tagName);
            project.addTag(tag);
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
        List<Project> projects = tag.getProjects();

        List<ProjectDTO> projectDTOS = projects
                .stream()
                .map(p -> modelMapper.map(p, ProjectDTO.class))
                .collect(Collectors.toList());
        return projectDTOS;
    }


    public List<ProjectDTO> findAll() {
        init();
        List<Project> projects = projectRepository.findAll();

        List<ProjectDTO> projectDTOS = projects
                .stream()
                .map(p -> modelMapper.map(p, ProjectDTO.class))
                .collect(Collectors.toList());

        return projectDTOS;
    }


    private void init() {
        List<Project> plist = projectMock.list(10).get();

        projectRepository.saveAll(plist);

    }


    @Transactional
    public ProjectDTO addReport(long id) {
        Project project = safelyGet(id);
        String sonarBaseUrl = project.getSonarQubeUrl();

        QualityReport qualityReport = qualityReportService.createReportFromUrl(sonarBaseUrl, project.getSonarComponentKey());
        project.setLastQualityReport(qualityReport.getUpdateDate());
        project.addQualityReport(qualityReport);

        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        return projectDTO;
    }

    @Transactional
    public ProjectDTO addReport(long id, InputStream inputStream) {
        Project project = safelyGet(id);
        String sonarBaseUrl = project.getSonarQubeUrl();

        QualityReport qualityReport = qualityReportService.createReportFromFile(inputStream);
        project.setLastQualityReport(qualityReport.getUpdateDate());
        project.addQualityReport(qualityReport);

        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        return projectDTO;
    }
}
