package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectRepoDTO;
import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.model.ProjectRepo;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.services.ProjectRepoService;
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
@RequestMapping("/projects/{repoId}/qualityReports")
public class QualityReportController {

    private static final Logger logger = LoggerFactory.getLogger(QualityReportController.class.getCanonicalName());

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProjectRepoService projectRepoService;

    @GetMapping
    public ResponseEntity<ProjectRepoDTO> getQualityReports(@PathVariable @Valid @NotNull Long repoId) {
        ProjectRepo projectRepo  = projectRepoService.updateProjectRepo(repoId);
        ProjectRepoDTO projectRepoDTO = modelMapper.map(projectRepo, ProjectRepoDTO.class);
        return new ResponseEntity<>(projectRepoDTO, HttpStatus.OK);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<ProjectRepoDTO> uploadReportFile(@RequestParam("file") MultipartFile file, @PathVariable @NotNull Long repoId,
                                                       @RequestParam("body") String body) throws IOException {
        InputStream inputStream = file.getInputStream();
        QualityReportDTO qualityReportDTO = new ObjectMapper().readValue(body, QualityReportDTO.class);
        QualityReport qualityReport = modelMapper.map(qualityReportDTO, QualityReport.class);

        ProjectRepo projectRepo  = projectRepoService.addQualityReport(repoId, inputStream, qualityReport);
        ProjectRepoDTO projectRepoDTO = modelMapper.map(projectRepo, ProjectRepoDTO.class);

        return new ResponseEntity<>(projectRepoDTO, HttpStatus.OK);
    }

}
