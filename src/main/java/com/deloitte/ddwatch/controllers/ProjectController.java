package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

}
