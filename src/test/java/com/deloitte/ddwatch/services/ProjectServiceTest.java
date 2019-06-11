package com.deloitte.ddwatch.services;


import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.ProjectRepo;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TagService tagService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createProject() {
        Project project = Project.builder()
                .name("test_project")
                .deliveryLead("")
                .deliveryLeadEmail("")
                .technicalLead("")
                .technicalLeadEmail("")
                .projectRepos(new HashSet<>())
                .build();

        when(projectService.create(project))
                .thenReturn(project);

        assertEquals(null, project.getTags());
    }

    @Test
    public void createProjectCheckProjectRepos() {
        Set<ProjectRepo> projectRepos = new HashSet<>();
        ProjectRepo projectRepo = ProjectRepo.builder()
                .name("projectRepo")
                .build();
        projectRepos.add(projectRepo);

        Project project = Project.builder()
                .name("test_project")
                .projectRepos(projectRepos)
                .build();

        when(projectService.create(project))
                .thenReturn(project);

        assertEquals(project, projectRepo.getProject());
    }

    @Test
    public void findProject() {
        Mockito.when(projectRepository.findById(1L)).thenReturn(
                Optional.of(Project.builder()
                        .name("test_project")
                        .deliveryLead("")
                        .deliveryLeadEmail("")
                        .technicalLead("")
                        .technicalLeadEmail("")
                        .projectRepos(new HashSet<>())
                        .build())
        );
        Project project = projectService.findById(1L);
        assertEquals("test_project", project.getName());
    }

}
