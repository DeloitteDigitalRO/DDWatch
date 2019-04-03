package com.deloitte.ddwatch.services;


import com.deloitte.ddwatch.model.SonarQubeReport;
import com.deloitte.ddwatch.repositories.SonarQubeReportRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SonarQubeReportService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    SonarQubeReportRepository sonarQubeReportRepository;

    public List<String> getProjectKeys() {
        String url = "http://localhost:9000/api/components/search?qualifiers=TRK";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        List<String> componentKeys = JsonPath.parse(response.getBody()).read("$..key");
        return componentKeys;
    }


    public SonarQubeReport createReportFromUrl(String baseUrl, String componentKey) {
        String resourceUrl = baseUrl + "/api/measures/component?componentKey=" + componentKey + "&metricKeys=ncloc,complexity,coverage,cognitive_complexity,duplicated_blocks," +
                "duplicated_lines,duplicated_lines_density,violations,code_smells,bugs,vulnerabilities,branch_coverage,line_coverage";


        ResponseEntity<String> response = restTemplate.getForEntity(resourceUrl, String.class);
        DocumentContext jsonContext = JsonPath.parse(response.getBody());

        SonarQubeReport sonarQubeReport = new SonarQubeReport();

        setMetrics(jsonContext, sonarQubeReport);
        setIssues(baseUrl, componentKey, sonarQubeReport);

        String defectDensity = sonarQubeReport.getTotalIssues().toString() + "/" + sonarQubeReport.getLinesOfCode().toString();
        sonarQubeReport.setDefectDensity(defectDensity);
        return sonarQubeReport;
    }


    public SonarQubeReport createReportFromFile(InputStream inputStream) {

        DocumentContext jsonContext  = JsonPath.parse(inputStream);

        SonarQubeReport sonarQubeReport = new SonarQubeReport();
        sonarQubeReport.setOverallCoverage(jsonContext.read("$.metrics.coverage"));
        sonarQubeReport.setCyclomaticComplexity(jsonContext.read("$.metrics.complexity"));
        sonarQubeReport.setLinesOfCode(jsonContext.read("$.metrics.ncloc"));
        sonarQubeReport.setCognitiveComplexity(jsonContext.read("$.metrics.cognitive_complexity"));
        sonarQubeReport.setDuplicatedBlocks(jsonContext.read("$.metrics.duplicated_blocks"));
        sonarQubeReport.setDuplicatedLines(jsonContext.read("$.metrics.duplicated_lines"));
        sonarQubeReport.setDuplicatedLinesDensity(jsonContext.read("$.metrics.duplicated_lines_density"));
        sonarQubeReport.setTotalIssues(jsonContext.read("$.metrics.violations"));
        sonarQubeReport.setTotalCodeSmells(jsonContext.read("$.metrics.code_smells"));
        sonarQubeReport.setTotalBugs(jsonContext.read("$.metrics.bugs"));
        sonarQubeReport.setTotalVulnerabilities(jsonContext.read("$.metrics.vulnerabilities"));
        sonarQubeReport.setConditionsCoverage(jsonContext.read("$.metrics.branch_coverage"));
        sonarQubeReport.setLineCoverage(jsonContext.read("$.metrics.line_coverage"));


        sonarQubeReport.setBlockerBugs(jsonContext.read("$.issues.bugs.severity.blockers"));
        sonarQubeReport.setCriticalBugs(jsonContext.read("$.issues.bugs.severity.critical"));
        sonarQubeReport.setMajorBugs(jsonContext.read("$.issues.bugs.severity.major"));
        sonarQubeReport.setMinorBugs(jsonContext.read("$.issues.bugs.severity.minor"));
        sonarQubeReport.setOtherBugs(jsonContext.read("$.issues.bugs.severity.info"));

        sonarQubeReport.setBlockerVulnerabilities(jsonContext.read("$.issues.vulnerabilities.severity.blockers"));
        sonarQubeReport.setCriticalVulnerabilities(jsonContext.read("$.issues.vulnerabilities.severity.critical"));
        sonarQubeReport.setMajorVulnerabilities(jsonContext.read("$.issues.vulnerabilities.severity.major"));
        sonarQubeReport.setMinorVulnerabilities(jsonContext.read("$.issues.vulnerabilities.severity.minor"));
        sonarQubeReport.setOtherVulnerabilities(jsonContext.read("$.issues.vulnerabilities.severity.info"));

        sonarQubeReport.setBlockerCodeSmells(jsonContext.read("$.issues.codeSmells.severity.blockers"));
        sonarQubeReport.setCriticalCodeSmells(jsonContext.read("$.issues.codeSmells.severity.critical"));
        sonarQubeReport.setMajorCodeSmells(jsonContext.read("$.issues.codeSmells.severity.major"));
        sonarQubeReport.setMinorCodeSmells(jsonContext.read("$.issues.codeSmells.severity.minor"));
        sonarQubeReport.setOtherCodeSmells(jsonContext.read("$.issues.codeSmells.severity.info"));

        String defectDensity = sonarQubeReport.getTotalIssues().toString() + "/" + sonarQubeReport.getLinesOfCode().toString();
        sonarQubeReport.setDefectDensity(defectDensity);

        sonarQubeReport.setUpdateDate(LocalDateTime.now());

        return sonarQubeReport;
    }
    


    private void setMetrics(DocumentContext jsonContext, SonarQubeReport sonarQubeReport) {
        String name = jsonContext.read("$.component.name");
        String key = jsonContext.read("$.component.key");

        sonarQubeReport.setName(name);
        sonarQubeReport.setKey(key);
        sonarQubeReport.setOverallCoverage(getDoubleSafely(jsonContext, "coverage"));
        sonarQubeReport.setCyclomaticComplexity(getIntSafely(jsonContext, "complexity"));
        sonarQubeReport.setLinesOfCode(getIntSafely(jsonContext, "ncloc"));
        sonarQubeReport.setCognitiveComplexity(getIntSafely(jsonContext, "cognitive_complexity"));
        sonarQubeReport.setDuplicatedBlocks(getIntSafely(jsonContext, "duplicated_blocks"));
        sonarQubeReport.setDuplicatedLines(getIntSafely(jsonContext, "duplicated_lines"));
        sonarQubeReport.setDuplicatedLinesDensity(getDoubleSafely(jsonContext, "duplicated_lines_density"));
        sonarQubeReport.setTotalIssues(getIntSafely(jsonContext, "violations"));
        sonarQubeReport.setTotalCodeSmells(getIntSafely(jsonContext, "code_smells"));
        sonarQubeReport.setTotalBugs(getIntSafely(jsonContext, "bugs"));
        sonarQubeReport.setTotalVulnerabilities(getIntSafely(jsonContext, "vulnerabilities"));
        sonarQubeReport.setConditionsCoverage(getDoubleSafely(jsonContext, "branch_coverage"));
        sonarQubeReport.setLineCoverage(getDoubleSafely(jsonContext, "line_coverage"));
        sonarQubeReport.setUpdateDate(LocalDateTime.now());
    }

    private Integer getIntSafely(DocumentContext jsonContext, String metricKey) {
        List<String> result = jsonContext.read("$['component']['measures'][?(@.metric == '" + metricKey + "')]['value']");
        return ObjectUtils.isEmpty(result)? null : Integer.parseInt(result.get(0));
    }

    private Double getDoubleSafely(DocumentContext jsonContext, String metricKey) {
        List<String> result = jsonContext.read("$['component']['measures'][?(@.metric == '" + metricKey + "')]['value']");
        return ObjectUtils.isEmpty(result)? null : Double.parseDouble(result.get(0));
    }


    private void setIssues(String baseUrl, String componentKey, SonarQubeReport sonarQubeReport) {

        String url = baseUrl + "/api/issues/search?componentKeys=" + componentKey;

        setBlockerBugs(url, sonarQubeReport);
        setCriticalBugs(url, sonarQubeReport);
        setMajorBugs(url, sonarQubeReport);
        setMinorBugs(url, sonarQubeReport);
        setOtherBugs(url, sonarQubeReport);

        setBlockerVulnerabilities(url, sonarQubeReport);
        setCriticalVulnerabilities(url, sonarQubeReport);
        setMajorVulnerabilities(url, sonarQubeReport);
        setMinorVulnerabilities(url, sonarQubeReport);
        setOtherVulnerabilities(url, sonarQubeReport);

        setBlockerCodeSmells(url, sonarQubeReport);
        setCriticalCodeSmells(url, sonarQubeReport);
        setMajorCodeSmells(url, sonarQubeReport);
        setMinorCodeSmells(url, sonarQubeReport);
        setOtherCodeSmells(url, sonarQubeReport);
    }

    private void setOtherBugs(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=BUG&severities=INFO&status=OPEN";
        sonarQubeReport.setOtherBugs(getNumberOfIssues(url));
    }

    private void setMajorBugs(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=BUG&severities=MAJOR&status=OPEN";
        sonarQubeReport.setMajorBugs(getNumberOfIssues(url));
    }

    private void setMinorBugs(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=BUG&severities=MINOR&status=OPEN";
        sonarQubeReport.setMinorBugs(getNumberOfIssues(url));
    }

    private void setCriticalBugs(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=BUG&severities=CRITICAL&status=OPEN";
        sonarQubeReport.setCriticalBugs(getNumberOfIssues(url));
    }

    private void setBlockerBugs(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=BUG&severities=BLOCKER&status=OPEN";
        sonarQubeReport.setBlockerBugs(getNumberOfIssues(url));
    }

    private void setOtherVulnerabilities(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=VULNERABILITY&severities=INFO&status=OPEN";
        sonarQubeReport.setOtherVulnerabilities(getNumberOfIssues(url));
    }

    private void setMajorVulnerabilities(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=VULNERABILITY&severities=MAJOR&status=OPEN";
        sonarQubeReport.setMajorVulnerabilities(getNumberOfIssues(url));
    }

    private void setMinorVulnerabilities(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=VULNERABILITY&severities=MINOR&status=OPEN";
        sonarQubeReport.setMinorVulnerabilities(getNumberOfIssues(url));
    }

    private void setCriticalVulnerabilities(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=VULNERABILITY&severities=CRITICAL&status=OPEN";
        sonarQubeReport.setCriticalVulnerabilities(getNumberOfIssues(url));
    }

    private void setBlockerVulnerabilities(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=VULNERABILITY&severities=BLOCKER&status=OPEN";
        sonarQubeReport.setBlockerVulnerabilities(getNumberOfIssues(url));
    }

    private Integer getNumberOfIssues(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        DocumentContext jsonContext = JsonPath.parse(response.getBody());
        return jsonContext.read("$.total");
    }

    private void setOtherCodeSmells(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=CODE_SMELL&severities=INFO&status=OPEN";
        sonarQubeReport.setOtherCodeSmells(getNumberOfIssues(url));
    }

    private void setMajorCodeSmells(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=CODE_SMELL&severities=MAJOR&status=OPEN";
        sonarQubeReport.setMajorCodeSmells(getNumberOfIssues(url));
    }

    private void setMinorCodeSmells(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=CODE_SMELL&severities=MINOR&status=OPEN";
        sonarQubeReport.setMinorCodeSmells(getNumberOfIssues(url));
    }

    private void setCriticalCodeSmells(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=CODE_SMELL&severities=CRITICAL&status=OPEN";
        sonarQubeReport.setCriticalCodeSmells(getNumberOfIssues(url));
    }

    private void setBlockerCodeSmells(String url, SonarQubeReport sonarQubeReport) {
        url = url + "&types=CODE_SMELL&severities=BLOCKER&status=OPEN";
        sonarQubeReport.setBlockerCodeSmells(getNumberOfIssues(url));
    }

}
