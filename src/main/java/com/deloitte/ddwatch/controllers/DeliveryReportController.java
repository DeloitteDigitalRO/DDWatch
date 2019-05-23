package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.MetricsReportDTO;
import com.deloitte.ddwatch.model.MetricsReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.services.ProjectReportService;
import com.deloitte.ddwatch.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@CrossOrigin
@RequestMapping("/projects/{projectId}/deliveryReports")
public class DeliveryReportController {

    private static Logger logger = LoggerFactory.getLogger(DeliveryReportController.class.getCanonicalName());

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectReportService projectReportService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MetricsReportDTO> addReport(@PathVariable @Valid @NotNull Long projectId,
                                                                @RequestPart MultipartFile projectReportFile) {
        logger.info("Uploaded report {} for project {}", projectReportFile.getName(), projectId);
        Project project = projectService.findById(projectId);
        MetricsReport metrics = projectReportService.parseProjectReportFile(projectReportFile);
        MetricsReport persistedMetrics = projectReportService.saveMetricsReport(metrics, project);
        return ResponseEntity.ok(projectReportService.convert(persistedMetrics));
    }

}
