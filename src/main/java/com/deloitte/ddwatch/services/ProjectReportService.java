package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.excel.ExcelParser;
import com.deloitte.ddwatch.model.MetricsReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.repositories.MetricsReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ProjectReportService {

    @Autowired
    private MetricsReportRepository metricsReportRepository;

    @Autowired
    private ExcelParser excelParser;

    @Autowired
    private ModelMapper modelMapper;

    public MetricsReport parseProjectReportFile(MultipartFile projectReportFile) {
        return excelParser.parseMetrics(projectReportFile);
    }

    @Transactional
    public MetricsReport saveMetricsReport(MetricsReport report, Project project) {
        report.setProject(project);
        report.setCreatedOn(LocalDateTime.now());
        return metricsReportRepository.save(report);
    }

    @Transactional
    public List<MetricsReport> getMetricsReport(Long projectId) {
        return metricsReportRepository.findByProjectIdOrderByCreatedOnDesc(projectId);
    }
}
