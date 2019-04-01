package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.services.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<String> create(@RequestBody ProjectDTO projectDTO) {

        return null;
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

}
