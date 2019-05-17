package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.excel.ExcelParser;
import com.deloitte.ddwatch.model.ProjectMetricsReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ProjectReportService {

    @Autowired
    private ExcelParser excelParser;

    public ProjectMetricsReport parseProjectReportFile(MultipartFile projectReportFile) {
        return excelParser.parseMetrics(projectReportFile);
    }
}
