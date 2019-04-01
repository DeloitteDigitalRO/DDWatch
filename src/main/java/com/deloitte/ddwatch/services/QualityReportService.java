package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.repositories.QualityReportRepository;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QualityReportService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    QualityReportRepository qualityReportRepository;

    public List<String> getProjectKeys() {
        String url = "http://localhost:9000/api/components/search?qualifiers=TRK";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        List<String> componentKeys = JsonPath.parse(response.getBody()).read("$..key");
        return componentKeys;
    }

    public List<QualityReport> createDemoReport() {
        List<QualityReport> reports = new ArrayList<>();
        for(String key : getProjectKeys()) {
            reports.add(createReport(key));
        }
        return reports;
    }

    public QualityReport refreshReport(String url) {
        String resourceUrl = url + "&metricKeys=ncloc,complexity,coverage,cognitive_complexity,duplicated_blocks," +
                "duplicated_lines,duplicated_lines_density,violations,code_smells,bugs,vulnerabilities,branch_coverage,line_coverage";

        return createReportFromUrl(resourceUrl);
    }

    public QualityReport createReport(String componentKey) {
        String fooResourceUrl = "http://localhost:9000/api/measures/component?componentKey=" + componentKey + "&metricKeys=ncloc,complexity,coverage,cognitive_complexity,duplicated_blocks," +
                "duplicated_lines,duplicated_lines_density,violations,code_smells,bugs,vulnerabilities,branch_coverage,line_coverage";

        return createReportFromUrl(fooResourceUrl);
    }

    public QualityReport createReportFromUrl(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        DocumentContext jsonContext = JsonPath.parse(response.getBody());
        String name = jsonContext.read("$.component.name");
        String key = jsonContext.read("$.component.key");

        QualityReport qualityReport = new QualityReport();

        qualityReport.setName(name);
        qualityReport.setKey(key);
        qualityReport.setOverallCoverage(getFloatSafely(jsonContext, "coverage"));
        qualityReport.setCyclomaticComplexity(getIntSafely(jsonContext, "complexity"));
        qualityReport.setLinesOfCode(getIntSafely(jsonContext, "ncloc"));
        qualityReport.setCognitiveComplexity(getIntSafely(jsonContext, "cognitive_complexity"));
        qualityReport.setDuplicatedBlocks(getIntSafely(jsonContext, "duplicated_blocks"));
        qualityReport.setDuplicatedLines(getIntSafely(jsonContext, "duplicated_lines"));
        qualityReport.setDuplicatedLinesDensity(getFloatSafely(jsonContext, "duplicated_lines_density"));
        qualityReport.setTotalIssues(getIntSafely(jsonContext, "violations"));
        qualityReport.setCodeSmels(getIntSafely(jsonContext, "code_smells"));
        qualityReport.setTotalBugs(getIntSafely(jsonContext, "bugs"));
        qualityReport.setTotalVulnerabilities(getIntSafely(jsonContext, "vulnerabilities"));
        qualityReport.setConditionsCoverage(getFloatSafely(jsonContext, "branch_coverage"));
        qualityReport.setLineCoverage(getFloatSafely(jsonContext, "line_coverage"));
        qualityReport.setUpdateDate(LocalDateTime.now());

        return qualityReport;
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
