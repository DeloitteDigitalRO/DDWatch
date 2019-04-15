package com.deloitte.ddwatch;

import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.Tag;
import com.deloitte.ddwatch.services.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.deloitte.ddwatch.mockunit.ProjectMock.generateTags;
import static java.util.stream.Collectors.toList;

@Component
public class DdwatchApplicationInit {

    private static final Logger logger = LoggerFactory.getLogger(DdwatchApplicationInit.class.getCanonicalName());

    @Autowired
    ProjectService projectService;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {

        final int numberOfMockProjects = 30;

        logger.info("Application ready.");
        logger.info("Building {} mock projects to be inserted in the database.", numberOfMockProjects);

        List<Project> projects = new ProjectMock()
                .list(numberOfMockProjects)
                .get();

        projects.stream()
                .forEach(p -> {
                    final Set<Tag> tags = generateTags();
                    projectService.create(p, tags);
                    logger.info("Added project {} into the databse with id {}, with tags: {}",
                            p.getName(),
                            p.getId(),
                            tags.stream()
                                .map(Tag::getName)
                                .collect(toList()));
                });

        logger.info("{} projects are now inserted into the database.", numberOfMockProjects);
    }
}
