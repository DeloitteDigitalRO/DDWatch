package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.dtos.QualityQuestionsAnswersDTO;
import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
public class QualityReportService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SonarQubeReportService sonarQubeReportService;


    public QualityReport createQualityReport(QualityReportDTO qualityReportDTO) {

        QualityReport qualityReport = new QualityReport();

        for(QualityQuestionsAnswersDTO answerDTO : qualityReportDTO.getQualityQuestionsAnswers()) {
            QualityQuestionsAnswers answer = new QualityQuestionsAnswers();
            answer.setQuestionId(answerDTO.getQuestionId());
            answer.setAnswer(answerDTO.getAnswer());
            qualityReport.addQualityQuestionAnswer(answer);
        }
        return qualityReport;
    }

    @Transactional
    public ProjectDTO addReport(long id, QualityReportDTO qualityReportDTO) {
        Project project = projectService.safelyGet(id);
        String sonarBaseUrl = project.getSonarQubeUrl();
        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromUrl(sonarBaseUrl, project.getSonarComponentKey());
        QualityReport qualityReport = createQualityReport(qualityReportDTO);
        ProjectDTO projectDTO = addReport(project, sonarQubeReport, qualityReport);
        return projectDTO;
    }

    @Transactional
    public ProjectDTO addReport(long id, InputStream inputStream, QualityReportDTO qualityReportDTO) throws IOException {
        Project project = projectService.safelyGet(id);
        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromFile(inputStream);
        QualityReport qualityReport = createQualityReport(qualityReportDTO);
        ProjectDTO projectDTO = addReport(project, sonarQubeReport, qualityReport);
        inputStream.close();
        return projectDTO;
    }

    @Transactional
    private ProjectDTO addReport(Project project, SonarQubeReport sonarQubeReport, QualityReport qualityReport) {
        qualityReport.setUpdateDate(LocalDateTime.now());
        project.setLastQualityReport(qualityReport.getUpdateDate());

        qualityReport.addSonarQubeReport(sonarQubeReport);
        project.addQualityReport(qualityReport);

        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        return projectDTO;
    }

}
