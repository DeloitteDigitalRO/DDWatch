package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.dtos.DeliveryReportDTO;
import com.deloitte.ddwatch.dtos.MetricsReportDTO;
import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.model.DeliveryReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.model.Status;
import com.deloitte.ddwatch.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ProjectReportControllerTest {

    private final String REPORT_FILE_PARAM = "deliveryReportFile";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ProjectService projectService;

    @Test
    public void uploadMetricsReport() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        // Create test project
        Project project = projectService.create(Project.builder()
                .name("test_project")
                .deliveryLead("")
                .deliveryLeadEmail("")
                .technicalLead("")
                .technicalLeadEmail("")
                .projectRepos(Set.of())
                .build()
        );

        // Expected result
        MetricsReportDTO reportDTO = MetricsReportDTO.builder()
                .id(project.getId() + 1)
                .empireTimeValue(100d)
                .empireTimeStatus(Status.GREEN.getExcelCode())
                .deliveryValue(90d)
                .deliveryStatus(Status.AMBER.getExcelCode())
                .attritionValue(15d)
                .attritionStatus(Status.AMBER.getExcelCode())
                .deliveryStatus(Status.AMBER.getExcelCode())
                .invoicingValue("No invoices outstanding before 30 days")
                .invoicingStatus(Status.GREEN.getExcelCode())
                .escalationRootCauseValue("People")
                .escalationSeniorityValue("Senior manager level")
                .escalationStatus(Status.AMBER.getExcelCode())
                .changeOrderValue("Covers the current day")
                .changeOrderStatus(Status.GREEN.getExcelCode())
                .testCoverageValue(70d)
                .testCoverageStatus(Status.AMBER.getExcelCode())
                .branchCoverageValue(100d)
                .branchCoverageStatus(Status.GREEN.getExcelCode())
                .duplicationDensityValue(50d)
                .duplicationDensityStatus(Status.GREEN.getExcelCode())
                .riskOverallStatus(Status.AMBER.getExcelCode())
                .riskOverallValue("MEDIUM RISK")
                .build();

        // Load test resource
        File excelFile = new ClassPathResource("excel/good_excel.xlsx").getFile();

        // Create mock multipart file
        MockMultipartFile mockMultipartFile = new MockMultipartFile(REPORT_FILE_PARAM, excelFile.getName(),
                MediaType.MULTIPART_FORM_DATA_VALUE, new FileInputStream(excelFile));


        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/projects/" + project.getId() + "/deliveryReports/upload")
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        DeliveryReportDTO deliveryReportDTO = objectMapper.readValue(
                result.getResponse().getContentAsString(), DeliveryReportDTO.class);
        assertEquals(reportDTO, deliveryReportDTO.getMetricsReport());
    }
}