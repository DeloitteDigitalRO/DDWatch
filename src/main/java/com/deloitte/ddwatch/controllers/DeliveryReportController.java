package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.DeliveryReportDTO;
import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.model.DeliveryReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{id}/deliveryReports")
@CrossOrigin
public class DeliveryReportController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ProjectDTO> addReport(@PathVariable String id, @RequestBody DeliveryReportDTO deliveryReportDTO) {
        DeliveryReport deliveryReport = modelMapper.map(deliveryReportDTO, DeliveryReport.class);

        Project project  = projectService.addDeliveryReport(Long.parseLong(id), deliveryReport);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }
}
