package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.DeliveryReportDTO;
import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.model.DeliveryReport;
import com.deloitte.ddwatch.model.MetricsReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class DeliveryReportServiceTest {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectService projectService;

    @Test
    public void test() throws Exception {

        Project project = new Project();
        project.setName("Test Name");
        project.setDescription("Test Description");
        project.setDeliveryLead("Test Delivery Lead");
        project.setDeliveryLeadEmail("delivery@email.com");
        project.setTechnicalLead("Test Technical Lead");
        project.setTechnicalLeadEmail("technical@lead.com");
        project.setDeliveryStatus(Status.GREEN);
        project.setQualityStatus(Status.AMBER);

        DeliveryReport deliveryReport1 = new DeliveryReport();
        deliveryReport1.setUpdateDate(LocalDateTime.now().minus(Period.ofMonths(2)));
        deliveryReport1.setMetricsReport(new MetricsReport());

        DeliveryReport deliveryReport2 = new DeliveryReport();
        deliveryReport2.setUpdateDate(LocalDateTime.now());
        deliveryReport2.setMetricsReport(new MetricsReport());

        DeliveryReport deliveryReport3 = new DeliveryReport();
        deliveryReport3.setUpdateDate(LocalDateTime.now().minus(Period.ofMonths(1)));
        deliveryReport3.setMetricsReport(new MetricsReport());

        project.addDeliveryReport(deliveryReport1);
        project.addDeliveryReport(deliveryReport2);
        project.addDeliveryReport(deliveryReport3);

        project = projectService.create(project);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/projects/" + project.getId())).andExpect(status().isOk()).andReturn();

        ProjectDTO projectDTO = objectMapper.readValue(result.getResponse().getContentAsString(), ProjectDTO.class);

        projectService.removeProject(project);

        List<@Valid DeliveryReportDTO> deliveryReports = projectDTO.getDeliveryReports();
        assertTrue(deliveryReports.get(0).getUpdateDate().isEqual(deliveryReport2.getUpdateDate()));
        assertTrue(deliveryReports.get(1).getUpdateDate().isEqual(deliveryReport3.getUpdateDate()));
        assertTrue(deliveryReports.get(2).getUpdateDate().isEqual(deliveryReport1.getUpdateDate()));

    }
}
