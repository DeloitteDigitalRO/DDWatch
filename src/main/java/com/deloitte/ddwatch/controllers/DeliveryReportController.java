package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.DeliveryReportDTO;
import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.model.DeliveryReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@CrossOrigin
@RequestMapping("/projects/{id}/deliveryReports")
public class DeliveryReportController {

    private static Logger logger = LoggerFactory.getLogger(DeliveryReportController.class.getCanonicalName());

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ProjectDTO> addReport(@PathVariable @Valid @NotNull Long id, @RequestBody @Valid DeliveryReportDTO deliveryReportDTO) {

        DeliveryReport deliveryReport = modelMapper.map(deliveryReportDTO, DeliveryReport.class);

        Project project  = projectService.addDeliveryReport(id, deliveryReport);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }
}
