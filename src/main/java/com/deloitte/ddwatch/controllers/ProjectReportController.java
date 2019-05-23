package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.MetricsReportDTO;
import com.deloitte.ddwatch.model.MetricsReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.services.ProjectReportService;
import com.deloitte.ddwatch.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/project_report")
public class ProjectReportController {

    @Autowired
    private ProjectReportService projectReportService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/metrics/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MetricsReportDTO> uploadMetricsReport(@RequestPart MultipartFile projectReportFile,
                                                                @RequestParam Long projectId) {
        log.info("Uploaded report {} for project {}", projectReportFile.getName(), projectId);
        Project project = projectService.findById(projectId);
        MetricsReport metrics = projectReportService.parseProjectReportFile(projectReportFile);
        MetricsReport persistedMetrics = projectReportService.saveMetricsReport(metrics, project);
        return ResponseEntity.ok(modelMapper.map(persistedMetrics, MetricsReportDTO.class));
    }

    @RequestMapping(value = "/metrics/get", method = RequestMethod.GET)
    public ResponseEntity<List<MetricsReportDTO>> getMetricsReport(@RequestParam Long projectId) {

        log.info("Get metrics report for project {}", projectId);
        Project project = projectService.findById(projectId);
        List<MetricsReport> metricsReports = projectReportService.getMetricsReport(project.getId());
        List<MetricsReportDTO> metricsReportDTOS = metricsReports
                .stream()
                .map(m -> modelMapper.map(m, MetricsReportDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(metricsReportDTOS);
    }

}
