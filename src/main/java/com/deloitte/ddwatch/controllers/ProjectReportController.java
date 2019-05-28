package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.DeliveryReportDTO;
import com.deloitte.ddwatch.model.DeliveryReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.services.ProjectReportService;
import com.deloitte.ddwatch.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

//    @RequestMapping(value = "/metrics/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<MetricsReportDTO> uploadMetricsReport(@RequestPart MultipartFile projectReportFile,
//                                                                @RequestParam Long projectId) {
//        log.info("Uploaded report {} for project {}", projectReportFile.getName(), projectId);
//        Project project = projectService.findById(projectId);
//        MetricsReport metrics = projectReportService.parseProjectReportFile(projectReportFile);
//        MetricsReport persistedMetrics = projectReportService.saveMetricsReport(metrics, project);
//        return ResponseEntity.ok(modelMapper.map(persistedMetrics, MetricsReportDTO.class));
//    }

    @RequestMapping(value = "/deliveryReports/get", method = RequestMethod.GET)
    public ResponseEntity<List<DeliveryReportDTO>> getDeliveryReports(@RequestParam Long projectId) {

        log.info("Get delivery reports for project {}", projectId);
        Project project = projectService.findById(projectId);
        List<DeliveryReport> deliveryReports = projectReportService.getDeliveryReports(project.getId());
        List<DeliveryReportDTO> deliveryReportDTOS = deliveryReports
                .stream()
                .map(m -> modelMapper.map(m, DeliveryReportDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(deliveryReportDTOS);
    }

}
