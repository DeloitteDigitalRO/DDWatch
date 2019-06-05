package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
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
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@CrossOrigin
@RequestMapping("/projectRepos/{repoId}/qualityReports")
public class QualityReportController {

    private static final Logger logger = LoggerFactory.getLogger(QualityReportController.class.getCanonicalName());

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProjectRepoService projectRepoService;

    @GetMapping
    public ResponseEntity<List<QualityReportDTO>> getQualityReports(@PathVariable @Valid @NotNull Long repoId) {
        ProjectRepo projectRepo = projectRepoService.getProjectRepo(repoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectRepo not found"));
        projectRepo  = projectRepoService.getUpdatedProjectRepo(projectRepo);
        List<QualityReportDTO> qualityReportDTOS = projectRepo.getQualityReports()
                .stream()
                .map(qr -> modelMapper.map(qr, QualityReportDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(qualityReportDTOS, HttpStatus.OK);
    }

    @GetMapping("/new")
    public ResponseEntity<QualityReportDTO> getNewQualityReport(@PathVariable @Valid @NotNull Long repoId) {
        ProjectRepo projectRepo = projectRepoService.getProjectRepo(repoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectRepo not found"));
        QualityReport qualityReport  = projectRepoService.getNewQualityReport(projectRepo);
        projectRepoService.updateProjectRepo(projectRepo, qualityReport);
        QualityReportDTO qualityReportDTO = modelMapper.map(qualityReport, QualityReportDTO.class);
        return new ResponseEntity<>(qualityReportDTO, HttpStatus.OK);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<ProjectRepoDTO> uploadReportFile(@RequestParam("file") MultipartFile file, @PathVariable @NotNull Long repoId,
                                                       @RequestParam("body") String body) throws IOException {

        ProjectRepo projectRepo = projectRepoService.getProjectRepo(repoId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectRepo not found"));

        InputStream inputStream = file.getInputStream();
        QualityReportDTO qualityReportDTO = new ObjectMapper().readValue(body, QualityReportDTO.class);
        QualityReport qualityReport = modelMapper.map(qualityReportDTO, QualityReport.class);
        projectRepoService.addQualityReport(projectRepo, inputStream, qualityReport);

        ProjectRepoDTO projectRepoDTO = modelMapper.map(projectRepo, ProjectRepoDTO.class);
        return new ResponseEntity<>(projectRepoDTO, HttpStatus.OK);
    }

}
