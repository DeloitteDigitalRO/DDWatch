package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.MetricsReportDTO;
import com.deloitte.ddwatch.excel.ExcelParser;
import com.deloitte.ddwatch.model.MetricsReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.Status;
import com.deloitte.ddwatch.repositories.MetricsReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ProjectReportService {

    @Autowired
    private MetricsReportRepository metricsReportRepository;

    @Autowired
    private ExcelParser excelParser;

    public MetricsReport parseProjectReportFile(MultipartFile projectReportFile) {
        return excelParser.parseMetrics(projectReportFile);
    }

    @Transactional
    public MetricsReport saveMetricsReport(MetricsReport report, Project project) {
        report.setProject(project);
        report.setCreatedOn(new Date());
        return metricsReportRepository.save(report);
    }

    @Transactional
    public List<MetricsReport> getMetricsReport(Long projectId) {
        return metricsReportRepository.findByProjectIdOrderByCreatedOnDesc(projectId);
    }

    public MetricsReportDTO convert(MetricsReport report) {
        ModelMapper modelMapper = new ModelMapper();

        Converter<Status, String> statusStringConverter = new Converter<Status, String>() {
            public String convert(MappingContext<Status, String> context) {
                return context.getSource() == null ? null : context.getSource().getExcelCode();
            }
        };

        modelMapper.addConverter(statusStringConverter);
        return modelMapper.map(report, MetricsReportDTO.class);
    }
}
