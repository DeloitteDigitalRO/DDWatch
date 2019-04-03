package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.SonarQubeReport;
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
    private SonarQubeReportService sonarQubeReportService;
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

        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromUrl(sonarBaseUrl, project.getSonarComponentKey());
        project.setLastQualityReport(sonarQubeReport.getUpdateDate());
        project.addQualityReport(sonarQubeReport);

        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        return projectDTO;
    }

    @Transactional
    public ProjectDTO addReport(long id, InputStream inputStream) {
        Project project = safelyGet(id);
        String sonarBaseUrl = project.getSonarQubeUrl();

        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromFile(inputStream);
        project.setLastQualityReport(sonarQubeReport.getUpdateDate());
        project.addQualityReport(sonarQubeReport);

        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        return projectDTO;
    }
}
