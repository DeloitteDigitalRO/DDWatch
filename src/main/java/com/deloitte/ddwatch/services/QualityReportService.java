package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
public class QualityReportService {

    private static final Logger logger = LoggerFactory.getLogger(QualityReportService.class.getCanonicalName());

    @Autowired
    private SonarQubeReportService sonarQubeReportService;

    @Transactional
    public QualityReport create(String sonarBaseUrl, String sonarComponentKey, QualityReport qualityReport) {
        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromUrl(sonarBaseUrl, sonarComponentKey);
        qualityReport.setQualityStatus(calculateStatus(sonarQubeReport));
        qualityReport.setUpdateDate(LocalDateTime.now());
        qualityReport.addSonarQubeReport(sonarQubeReport);
        return qualityReport;
    }

    @Transactional
    public QualityReport create(InputStream inputStream, QualityReport qualityReport) throws IOException {
        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromFile(inputStream);
        qualityReport.setQualityStatus(calculateStatus(sonarQubeReport));
        qualityReport.setUpdateDate(LocalDateTime.now());
        qualityReport.addSonarQubeReport(sonarQubeReport);
        inputStream.close();
        return qualityReport;
    }

    public QualityReport create(String sonarBaseUrl, String sonarComponentKey, ProjectRepo projectRepo) {
        QualityReport qualityReport = new QualityReport();
        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromUrl(sonarBaseUrl, sonarComponentKey);
        qualityReport.setQualityStatus(calculateStatus(sonarQubeReport));
        qualityReport.setUpdateDate(LocalDateTime.now());
        qualityReport.addSonarQubeReport(sonarQubeReport);
        qualityReport.setProjectRepo(projectRepo);
        projectRepo.addQualityReport(qualityReport);
        return qualityReport;
    }

    private Status calculateStatus(SonarQubeReport sonarQubeReport) {
        Status statusForCoverage = Status.getStatusByOverallCoverage(sonarQubeReport.getOverallCoverage());
        Status statusForDefectDensity = Status.getStatusByDefectDensity(sonarQubeReport.getDefectDensity());

        if (statusForCoverage.getPriority() < statusForDefectDensity.getPriority()) {
            return statusForCoverage;
        } else {
            return statusForDefectDensity;
        }
    }
}
