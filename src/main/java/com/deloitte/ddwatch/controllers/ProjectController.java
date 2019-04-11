package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.Tag;
import com.deloitte.ddwatch.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody ProjectDTO projectDTO) {
        Project project = modelMapper.map(projectDTO, Project.class);
        Set<Tag> tags = projectDTO.getTags()
                .stream()
                .map(t -> modelMapper.map(t, Tag.class))
                .collect(Collectors.toSet());

        project = projectService.create(project, tags);
        return new ResponseEntity<>(project.getId(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAll() {
        List<Project> projects = projectService.findAll();
        List<ProjectDTO> projectDTOS = projects
                .stream()
                .map(p -> modelMapper.map(p, ProjectDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity(projectDTOS, HttpStatus.OK);
    }


    @GetMapping("/query")
    public ResponseEntity<List<ProjectDTO>> getByTag(@RequestParam String tag) {
        Set<Project> projects = projectService.findByTag(tag);
        List<ProjectDTO> projectDTOS = projects
                .stream()
                .map(p -> modelMapper.map(p, ProjectDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity(projectDTOS, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@PathVariable String id) {
        Project project = projectService.findById(Long.parseLong(id));
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

}
