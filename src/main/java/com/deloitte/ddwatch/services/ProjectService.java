package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectMock projectMock;

    public Project create(Project project) {
        project = projectRepository.save(project);
        return project;
    }

    public Project readById(String id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            throw new RuntimeException("No such project found");
        }
        return project.get();
    }

    public List<Project> getAll() {

        List<Project> projects = projectRepository.findAll();
        return projects;
    }



    private void init() {
        List<Project> plist = projectMock.list(10).get();

        projectRepository.saveAll(plist);

    }

}
