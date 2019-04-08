package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
public class QualityReportService {

    @Autowired
    private SonarQubeReportService sonarQubeReportService;

    @Transactional
    public QualityReport create(String sonarBaseUrl, String sonarComponentKey, QualityReport qualityReport) {
        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromUrl(sonarBaseUrl, sonarComponentKey);
        qualityReport.setUpdateDate(LocalDateTime.now());
        qualityReport.addSonarQubeReport(sonarQubeReport);
        return qualityReport;
    }

    @Transactional
    public QualityReport create(InputStream inputStream, QualityReport qualityReport) throws IOException {
        SonarQubeReport sonarQubeReport = sonarQubeReportService.createReportFromFile(inputStream);
        qualityReport.setUpdateDate(LocalDateTime.now());
        qualityReport.addSonarQubeReport(sonarQubeReport);
        inputStream.close();
        return qualityReport;
    }
}
