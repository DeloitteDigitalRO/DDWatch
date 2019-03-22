package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QualityReportService {

    @Autowired
    RestTemplate restTemplate;

    public List<String> getProjectKeys() {
        String url = "http://localhost:9000/api/components/search?qualifiers=TRK";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        List<String> componentKeys = JsonPath.parse(response.getBody()).read("$..key");
        return componentKeys;
    }

    public List<QualityReportDTO> createDemoReport() {
        List<QualityReportDTO> reportDTOS = new ArrayList<>();
        for(String key : getProjectKeys()) {
            reportDTOS.add(createReport(key));
        }
        return reportDTOS;
    }

    public QualityReportDTO createReport(String componentKey) {
        String fooResourceUrl = "http://localhost:9000/api/measures/component?componentKey=" + componentKey + "&metricKeys=ncloc,complexity,coverage,cognitive_complexity,duplicated_blocks," +
                "duplicated_lines,duplicated_lines_density,violations,code_smells,bugs,vulnerabilities,branch_coverage,line_coverage";

        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
        DocumentContext jsonContext = JsonPath.parse(response.getBody());
        String name = jsonContext.read("$.component.name");
        String key = jsonContext.read("$.component.key");

        QualityReportDTO qualityReportDTO = new QualityReportDTO();

        qualityReportDTO.setName(name);
        qualityReportDTO.setKey(key);
        qualityReportDTO.setOverallCoverage(getFloatSafely(jsonContext, "coverage"));
        qualityReportDTO.setCyclomaticComplexity(getIntSafely(jsonContext, "complexity"));
        qualityReportDTO.setLinesOfCode(getIntSafely(jsonContext, "ncloc"));
        qualityReportDTO.setCognitiveComplexity(getIntSafely(jsonContext, "cognitive_complexity"));
        qualityReportDTO.setDuplicatedBlocks(getIntSafely(jsonContext, "duplicated_blocks"));
        qualityReportDTO.setDuplicatedLines(getIntSafely(jsonContext, "duplicated_lines"));
        qualityReportDTO.setDuplicatedLinesDensity(getFloatSafely(jsonContext, "duplicated_lines_density"));
        qualityReportDTO.setTotalIssues(getIntSafely(jsonContext, "violations"));
        qualityReportDTO.setCodeSmels(getIntSafely(jsonContext, "code_smells"));
        qualityReportDTO.setTotalBugs(getIntSafely(jsonContext, "bugs"));
        qualityReportDTO.setTotalVulnerabilities(getIntSafely(jsonContext, "vulnerabilities"));
        qualityReportDTO.setConditionsCoverage(getFloatSafely(jsonContext, "branch_coverage"));
        qualityReportDTO.setLineCoverage(getFloatSafely(jsonContext, "line_coverage"));

        return qualityReportDTO;
    }

    private Integer getIntSafely(DocumentContext jsonContext, String metricKey) {
        List<String> result = jsonContext.read("$['component']['measures'][?(@.metric == '" + metricKey + "')]['value']");
        return ObjectUtils.isEmpty(result)? null : Integer.parseInt(result.get(0));
    }

    private Float getFloatSafely(DocumentContext jsonContext, String metricKey) {
        List<String> result = jsonContext.read("$['component']['measures'][?(@.metric == '" + metricKey + "')]['value']");
        return ObjectUtils.isEmpty(result)? null : Float.parseFloat(result.get(0));
    }


}
