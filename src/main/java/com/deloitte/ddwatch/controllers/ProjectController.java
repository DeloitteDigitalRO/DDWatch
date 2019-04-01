package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    ProjectService projectService;
//    @Autowired
//    ModelMapper modelMapper;


    @PostMapping
    public ResponseEntity<Long> create(@RequestBody ProjectDTO projectDTO) {
        Project project = projectService.create(projectDTO);
        return new ResponseEntity<>(project.getId(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAll() {
        ModelMapper modelMapper = new ModelMapper();
        List<ProjectDTO> projectDTOS = projectService
                                        .getAll()
                                        .stream()
                                        .map(p -> modelMapper.map(p, ProjectDTO.class))
                                        .collect(Collectors.toList());
        return new ResponseEntity(projectDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@PathVariable String id) {
        ModelMapper modelMapper = new ModelMapper();

        ProjectDTO projectDTO = modelMapper.map(projectService.readById(Long.parseLong(id)), ProjectDTO.class);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

    @PutMapping("/{id}/refreshReport")
    public ResponseEntity<ProjectDTO> refreshReport(@PathVariable String id) {
        ModelMapper modelMapper = new ModelMapper();

        Project project = projectService.refreshReport(Long.parseLong(id));
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

}
