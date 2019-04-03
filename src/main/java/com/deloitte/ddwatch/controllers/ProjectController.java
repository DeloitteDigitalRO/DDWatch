package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.services.ProjectService;
import com.deloitte.ddwatch.services.QualityReportService;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;



    @PostMapping
    public ResponseEntity<Long> create(@RequestBody ProjectDTO projectDTO) {
        Project project = projectService.create(projectDTO);
        return new ResponseEntity<>(project.getId(), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAll() {
        List<ProjectDTO> projectDTOS = projectService.findAll();
        return new ResponseEntity(projectDTOS, HttpStatus.OK);
    }


    @GetMapping("/query")
    public ResponseEntity<List<ProjectDTO>> getByTag(@RequestParam String tag) {
        List<ProjectDTO> projectDTOS = projectService.findByTag(tag);
        return new ResponseEntity(projectDTOS, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@PathVariable String id) {
        ProjectDTO projectDTO = projectService.findById(Long.parseLong(id));
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }


    @PutMapping("/{id}/addReport")
    public ResponseEntity<ProjectDTO> addReport(@PathVariable String id) {
        ProjectDTO projectDTO  = projectService.addReport(Long.parseLong(id));
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}/uploadReportFile")
    public ResponseEntity<ProjectDTO> uploadReportFile(@RequestParam("file") MultipartFile file, @PathVariable String id) throws IOException {


        InputStream inputStream = file.getInputStream();

        ProjectDTO projectDTO = projectService.addReport(Long.parseLong(id), inputStream);

        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }
}
