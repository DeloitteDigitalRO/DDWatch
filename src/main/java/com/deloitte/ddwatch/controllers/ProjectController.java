package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.Tag;
import com.deloitte.ddwatch.services.ProjectService;
import net.andreinc.jbvext.annotations.str.Alphanumeric;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/projects")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class.getCanonicalName());

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody @Valid ProjectDTO projectDTO) {
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
    public ResponseEntity<List<ProjectDTO>> getByTag(@RequestParam @Alphanumeric @Size(min = 2, max = 32) String tag) {
        Set<Project> projects = projectService.findByTag(tag);
        List<ProjectDTO> projectDTOS = projects
                .stream()
                .map(p -> modelMapper.map(p, ProjectDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity(projectDTOS, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getById(@PathVariable @NotNull Long id) {
        Project project = projectService.findById(id);
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        return new ResponseEntity<>(projectDTO, HttpStatus.OK);
    }

}
