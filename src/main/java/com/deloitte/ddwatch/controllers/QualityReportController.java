package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.services.ProjectService;
import com.deloitte.ddwatch.services.SonarQubeReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/projects/{id}")
public class QualityReportController {

    @Autowired
    SonarQubeReportService sonarQubeReportService;
    @Autowired
    private ProjectService projectService;

    @PutMapping("/addReport")
    public ResponseEntity<ProjectDTO> addReport(@PathVariable String id) {
        ProjectDTO projectDTO  = projectService.addReport(Long.parseLong(id));
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @PutMapping("/uploadReportFile")
    public ResponseEntity<ProjectDTO> uploadReportFile(@RequestParam("file") MultipartFile file, @PathVariable String id) throws IOException {
        InputStream inputStream = file.getInputStream();
        ProjectDTO projectDTO = projectService.addReport(Long.parseLong(id), inputStream);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }
}
