package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.services.ProjectService;
import com.deloitte.ddwatch.services.QualityReportService;
import com.deloitte.ddwatch.services.SonarQubeReportService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/projects/{id}")
public class QualityReportController {

    @Autowired
    private SonarQubeReportService sonarQubeReportService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private QualityReportService qualityReportService;
    @Autowired
    private ModelMapper modelMapper;

    @PutMapping("/addReport")
    public ResponseEntity<ProjectDTO> addReport(@PathVariable String id, @RequestBody QualityReportDTO qualityReportDTO) {
        QualityReport qualityReport = modelMapper.map(qualityReportDTO, QualityReport.class);

        Project project  = projectService.addReport(Long.parseLong(id), qualityReport);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @PutMapping("/uploadReportFile")
    public ResponseEntity<ProjectDTO> uploadReportFile(@RequestParam("file") MultipartFile file, @PathVariable String id, @RequestParam("body") String body) throws IOException {
        InputStream inputStream = file.getInputStream();
        QualityReportDTO qualityReportDTO = new Gson().fromJson(body, QualityReportDTO.class);
        QualityReport qualityReport = modelMapper.map(qualityReportDTO, QualityReport.class);

        Project project = projectService.addReport(Long.parseLong(id), inputStream, qualityReport);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }
}
