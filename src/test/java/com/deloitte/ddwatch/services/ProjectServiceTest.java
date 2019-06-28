package com.deloitte.ddwatch.services;


import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.ProjectRepo;
import com.deloitte.ddwatch.model.Status;
import com.deloitte.ddwatch.model.Tag;
import com.deloitte.ddwatch.repositories.ProjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.rest.core.annotation.Description;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        projectService.findById(1L);
    }

    @Test(expected = RuntimeException.class)
    public void findByIdShouldThrowExceptionWhenIdIsNull(){
        //arrange
        when(projectRepository.findById(null)).thenReturn(Optional.empty());

        //act
        projectService.findById(null);
    }

    @Test
    public void findByIdShouldReturnProperProject() {
        //arrange
        Project project = Project.builder()
                .id(123L)
                .name("test_project1")
                .deliveryLead("test_lead")
                .deliveryLeadEmail("test_delivery_lead@deloitte.com")
                .technicalLead("test_technical_lead")
                .technicalLeadEmail("test_technical_lead@deloitte.com")
                .deliveryStatus(Status.GREEN)
                .deliveryReports(List.of())
                .projectRepos(Set.of())
                .build();
        when(projectRepository.findById(123L)).thenReturn(Optional.of(project));

        //act
        Project projectReturned = projectService.findById(project.getId());

        //assert
        assertNotNull(projectReturned);
        assertEquals(project, projectReturned);
        assertEquals(project.getName(), projectReturned.getName());
        assertEquals(project.getDeliveryLead(), projectReturned.getDeliveryLead());
        assertEquals(project.getDeliveryLeadEmail(), projectReturned.getDeliveryLeadEmail());
        assertEquals(project.getTechnicalLead(), projectReturned.getTechnicalLead());
        assertEquals(project.getTechnicalLeadEmail(), projectReturned.getTechnicalLeadEmail());
        assertEquals(project.getDeliveryStatus(), projectReturned.getDeliveryStatus());
        assertEquals(project.getDeliveryReports(),projectReturned.getDeliveryReports());
    }

    @Test
    public void findByTagShouldReturnAProject(){
        //arrange
        Set<Project> projects = Set.of(
                Project.builder().name("Test Project 1").build(),
                Project.builder().name("Test Project 2").build(),
                Project.builder().name("Test Project 3").build());
        Tag tag = Tag.builder()
                .name("test_tag")
                .projects(projects)
                .build();
        when(tagService.safelyGetByName("test_name")).thenReturn(tag);

        //act
        Set<Project> projectsReturned = projectService.findByTag("test_name");

        //assert
        assertEquals(tag.getProjects(),projectsReturned);
    }

    @Test (expected = RuntimeException.class)
    public void findByTagShouldThrowException(){
        //arrange
        when(tagService.safelyGetByName("test_name")).thenThrow(RuntimeException.class);

        //act
        Set<Project> projectsReturned = projectService.findByTag("test_name");
    }

    @Test
    public void removeShouldDeleteProject(){
        //arrange
        Project projectToBeRemoved = Project.builder().name("test_remove_project").build();
        doNothing().when(projectRepository).delete(projectToBeRemoved);

        //act
        projectService.removeProject(projectToBeRemoved);

        //assert
        verify(projectRepository, times(1)).delete(projectToBeRemoved);
    }

}
