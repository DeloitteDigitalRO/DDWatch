package com.deloitte.ddwatch;

import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DdwatchApplicationInit {

    @Autowired
    ProjectService projectService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        new ProjectMock()
                .list(30)
                .get()
                .stream()
                .forEach(projectService::create);
    }
}
