package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.dtos.QualityQuestionsAnswersDTO;
import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.dtos.SonarQubeReportDTO;
import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.model.*;
import com.deloitte.ddwatch.model.json.Question;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private SonarQubeReportService sonarQubeReportService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private QualityReportService qualityReportService;
    @Autowired
    private Map<String, Question> qualityQuestionsMap;


    public Project create(ProjectDTO projectDTO) {
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
        convertAnswers(projectDTO, project);

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
        List<Project> projects = projectRepository.findAll();

        List<ProjectDTO> projectDTOS = projects
                .stream()
                .map(p -> modelMapper.map(p, ProjectDTO.class))
                .collect(Collectors.toList());

        return projectDTOS;
    }



    @Transactional
    public ProjectDTO addReport(long id, QualityReportDTO qualityReportDTO) {
        Project project = safelyGet(id);
        String sonarBaseUrl = project.getSonarQubeUrl();
        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromUrl(sonarBaseUrl, project.getSonarComponentKey());
        QualityReport qualityReport = qualityReportService.test(qualityReportDTO);
        ProjectDTO projectDTO = addReport(project, sonarQubeReport, qualityReport);
        return projectDTO;
    }

    @Transactional
    public ProjectDTO addReport(long id, InputStream inputStream, QualityReportDTO qualityReportDTO) throws IOException {
        Project project = safelyGet(id);
        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromFile(inputStream);
        QualityReport qualityReport = qualityReportService.test(qualityReportDTO);
        ProjectDTO projectDTO = addReport(project, sonarQubeReport, qualityReport);
        inputStream.close();
        return projectDTO;
    }

    private ProjectDTO addReport(Project project, SonarQubeReport sonarQubeReport, QualityReport qualityReport) {
        qualityReport.setUpdateDate(LocalDateTime.now());
        project.setLastQualityReport(qualityReport.getUpdateDate());

        qualityReport.addSonarQubeReport(sonarQubeReport);
        project.addQualityReport(qualityReport);

        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        convertAnswers(projectDTO, project);

        return projectDTO;
    }

    @Transactional
    public ProjectDTO test(long id, QualityReportDTO qualityReportDTO) {
        Project project = safelyGet(id);

        QualityReport qualityReport = qualityReportService.test(qualityReportDTO);
        project.addQualityReport(qualityReport);



        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        convertAnswers(projectDTO, project);


        return projectDTO;
    }



    private void convertAnswers(ProjectDTO projectDTO, Project project) {
        List<QualityReportDTO> qualityReportDTOS = new ArrayList<>();
        for(QualityReport qr : project.getQualityReports()) {
            QualityReportDTO qrDTO = new QualityReportDTO();
            SonarQubeReport sonarQubeReport = qr.getSonarQubeReport();
            if (sonarQubeReport != null) {
                qrDTO.setSonarQubeReport(modelMapper.map(qr.getSonarQubeReport(), SonarQubeReportDTO.class));
            }
            qrDTO.setUpdateDate(qr.getUpdateDate());
            List<QualityQuestionsAnswersDTO> answersDTOS = new ArrayList<>();
            for(QualityQuestionsAnswers answer : qr.getQuestionsAnswers()) {
                QualityQuestionsAnswersDTO answerDTO = new QualityQuestionsAnswersDTO();
                answerDTO.setQuestionId(answer.getQuestionId());
                answerDTO.setText(qualityQuestionsMap.get(answerDTO.getQuestionId()).getText());
                answerDTO.setAnswer(answer.getAnswer());
                answersDTOS.add(answerDTO);
            }
            qrDTO.setQualityQuestionsAnswers(answersDTOS);
            qualityReportDTOS.add(qrDTO);
        }
        projectDTO.setQualityReports(qualityReportDTOS);
    }
}
