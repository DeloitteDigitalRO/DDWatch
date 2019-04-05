package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.services.ProjectService;
import com.deloitte.ddwatch.services.QualityReportService;
import com.deloitte.ddwatch.services.SonarQubeReportService;
import com.google.gson.Gson;
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

    @PutMapping("/addReport")
    public ResponseEntity<ProjectDTO> addReport(@PathVariable String id, @RequestBody QualityReportDTO qualityReportDTO) {
        ProjectDTO projectDTO  = qualityReportService.addReport(Long.parseLong(id), qualityReportDTO);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @PutMapping("/uploadReportFile")
    public ResponseEntity<ProjectDTO> uploadReportFile(@RequestParam("file") MultipartFile file, @PathVariable String id, @RequestParam("body") String body) throws IOException {
        InputStream inputStream = file.getInputStream();
        QualityReportDTO qualityReportDTO = new Gson().fromJson(body, QualityReportDTO.class);
        ProjectDTO projectDTO = qualityReportService.addReport(Long.parseLong(id), inputStream, qualityReportDTO);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

//    @PutMapping("/test")
//    public ResponseEntity<ProjectDTO> test(@PathVariable String id, @RequestBody QualityReportDTO qualityReportDTO) {
//        ProjectDTO projectDTO = qualityReportService.createQualityReport(Long.parseLong(id), qualityReportDTO);
//        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
//    }
}
