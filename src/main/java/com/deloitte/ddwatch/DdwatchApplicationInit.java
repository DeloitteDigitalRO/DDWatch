package com.deloitte.ddwatch;

import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.deloitte.ddwatch.mockunit.ProjectMock.generateTags;

@Component
public class DdwatchApplicationInit {

    @Autowired
    ProjectService projectService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        List<Project> projects = new ProjectMock()
                .list(30)
                .get();


        projects.stream()
                .forEach(p -> projectService.create(p, generateTags()));
    }
}
