package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.DeliveryReportDTO;
import com.deloitte.ddwatch.model.DeliveryReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.services.DeliveryReportService;
import com.deloitte.ddwatch.services.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@CrossOrigin
@RequestMapping("/projects/{projectId}/deliveryReports")
public class DeliveryReportController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private DeliveryReportService deliveryReportService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DeliveryReportDTO> uploadeDeliveryReport(@PathVariable @Valid @NotNull Long projectId,
                                                                   @RequestPart MultipartFile deliveryReportFile) throws IOException {
        log.info("Uploaded report {} for project {}", deliveryReportFile.getName(), projectId);
        Project project = projectService.findProject(projectId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        DeliveryReport deliveryReport = deliveryReportService.createDeliveryReport(project, deliveryReportFile.getInputStream());
        return ResponseEntity.ok(modelMapper.map(deliveryReport, DeliveryReportDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryReportDTO>> getDeliveryReports(@PathVariable Long projectId) {
        log.info("Get delivery reports for project {}", projectId);
        List<DeliveryReport> deliveryReports = deliveryReportService.getDeliveryReports(projectId);
        List<DeliveryReportDTO> deliveryReportDTOS = deliveryReports
                .stream()
                .map(m -> modelMapper.map(m, DeliveryReportDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(deliveryReportDTOS);
    }
}
