package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;

@Validated
@RestController
@CrossOrigin
@RequestMapping("/projects/{id}/qualityReports")
public class QualityReportController {

    private static final Logger logger = LoggerFactory.getLogger(QualityReportController.class.getCanonicalName());

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ProjectDTO> addReport(@PathVariable @Valid @NotNull Long id, @RequestBody @Valid QualityReportDTO qualityReportDTO) {
        QualityReport qualityReport = modelMapper.map(qualityReportDTO, QualityReport.class);

        Project project  = projectService.addReport(id, qualityReport);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<ProjectDTO> uploadReportFile(@RequestParam("file") MultipartFile file, @PathVariable @NotNull Long id, @RequestParam("body") String body) throws IOException {
        InputStream inputStream = file.getInputStream();
        QualityReportDTO qualityReportDTO = new ObjectMapper().readValue(body, QualityReportDTO.class);
        QualityReport qualityReport = modelMapper.map(qualityReportDTO, QualityReport.class);

        Project project = projectService.addReport(id, inputStream, qualityReport);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }
}
