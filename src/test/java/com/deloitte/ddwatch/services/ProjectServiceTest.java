package com.deloitte.ddwatch.services;


import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.ProjectRepo;
import com.deloitte.ddwatch.model.Tag;
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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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
    public void createProjectWithNoTagsShouldReturnProperProject() {
        // Given initial conditions
        Set<ProjectRepo> projectRepos = Set.of(
                ProjectRepo.builder()
                        .name("test_repo")
                        .build()
        );

        Project project = Project.builder()
                .name("test_project")
                .projectRepos(projectRepos)
                .build();

        // When
        when(projectRepository.save(project)).thenReturn(project);
        doNothing().when(tagService).setTags(any(), any());

        // Then
        Project result = projectService.create(project);
        assertEquals(project, result);

        ProjectRepo resultRepo = (ProjectRepo) result.getProjectRepos().toArray()[0];
        assertEquals(project, resultRepo.getProject());

        assertNull(result.getTags());
    }

    @Test
    public void createProjectTagsShouldReturnProperProject() {
        // Given initial conditions
        Set<Tag> tags = Set.of(
                    Tag.builder()
                        .name("test_tag")
                        .build()
        );

        Project project = Project.builder()
                .name("test_project")
                .projectRepos(Set.of())
                .build();

        // When
        doNothing().when(tagService).setTags(project, tags);
        when(projectRepository.save(project)).thenReturn(project);

        // Then
        Project result = projectService.create(project, tags);
        assertEquals(project, result);
    }

    @Test(expected = RuntimeException.class)
    public void findByIdShouldThrowExceptionWhenProjectNotFound() {
        // When
        Mockito.when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        projectService.findById(1L);
    }
}
