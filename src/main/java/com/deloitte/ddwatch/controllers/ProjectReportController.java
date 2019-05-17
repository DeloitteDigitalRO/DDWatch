package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.model.ProjectMetricsReport;
import com.deloitte.ddwatch.services.ProjectReportService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/project_report")
public class ProjectReportController {

    @Autowired
    private ProjectReportService projectReportService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectMetricsReport> uploadProjectReport(@RequestPart MultipartFile projectReportFile) {
       log.info("Uploaded report: {}", projectReportFile.getName());
       ProjectMetricsReport metrics = projectReportService.parseProjectReportFile(projectReportFile);
       return ResponseEntity.ok(metrics);
    }
}
