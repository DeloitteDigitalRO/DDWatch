package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.model.Tag;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import com.deloitte.ddwatch.repositories.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectMock projectMock;
    @Autowired
    QualityReportService qualityReportService;
    @Autowired
    TagRepository tagRepository;


    public Project create(ProjectDTO projectDTO) {
        ModelMapper modelMapper = new ModelMapper();

        Project project = modelMapper.map(projectDTO, Project.class);

        for(String tagName : projectDTO.getTagNames()) {
            Tag tag = tagRepository.findByName(tagName);
            project.addTag(tag);
        }

        project = projectRepository.save(project);
        return project;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public Project readById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new RuntimeException("No such project found");
        }
        return project.get();
    }

    public List<Project> getAll() {
        init();
        List<Project> projects = projectRepository.findAll();
        return projects;
    }



    private void init() {
        List<Project> plist = projectMock.list(10).get();

        projectRepository.saveAll(plist);

    }

    @Transactional
    public Project refreshReport(long id) {
        Project project = readById(id);
        String sonarBaseUrl = project.getSonarQubeUrl();

        QualityReport qualityReport = qualityReportService.refreshReport(sonarBaseUrl);
        project.setLastQualityReport(qualityReport.getUpdateDate());
        project.addQualityReport(qualityReport);

        return project;
    }
}
