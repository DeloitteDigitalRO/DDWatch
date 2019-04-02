package com.deloitte.ddwatch.services;


import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.repositories.QualityReportRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public QualityReport createReport(String baseUrl, String componentKey) {
        String resourceUrl = baseUrl + "/api/measures/component?componentKey=" + componentKey + "&metricKeys=ncloc,complexity,coverage,cognitive_complexity,duplicated_blocks," +
                "duplicated_lines,duplicated_lines_density,violations,code_smells,bugs,vulnerabilities,branch_coverage,line_coverage";

        QualityReport qualityReport = createReportFromUrl(resourceUrl);
        setIssues(baseUrl, componentKey, qualityReport);
        String defectDensity = qualityReport.getTotalIssues().toString() + "/" + qualityReport.getLinesOfCode().toString();
        qualityReport.setDefectDensity(defectDensity);
        return qualityReport;
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


    public void setIssues(String baseUrl, String componentKey, QualityReport qualityReport) {

        String url = baseUrl + "/api/issues/search?componentKeys=" + componentKey;

        setBlockerBugs(url, qualityReport);
        setCriticalBugs(url, qualityReport);
        setMajorBugs(url, qualityReport);
        setMinorBugs(url, qualityReport);
        setOtherBugs(url, qualityReport);

        setBlockerVulnerabilities(url, qualityReport);
        setCriticalVulnerabilities(url, qualityReport);
        setMajorVulnerabilities(url, qualityReport);
        setMinorVulnerabilities(url, qualityReport);
        setOtherVulnerabilities(url, qualityReport);

    }

    private void setOtherBugs(String url, QualityReport qualityReport) {
        url = url + "&types=BUG&severities=INFO&status=OPEN";
        qualityReport.setOtherBugs(getNumberOfIssues(url));
    }

    private void setMajorBugs(String url, QualityReport qualityReport) {
        url = url + "&types=BUG&severities=MAJOR&status=OPEN";
        qualityReport.setMajorBugs(getNumberOfIssues(url));
    }

    private void setMinorBugs(String url, QualityReport qualityReport) {
        url = url + "&types=BUG&severities=MINOR&status=OPEN";
        qualityReport.setMinorBugs(getNumberOfIssues(url));
    }

    private void setCriticalBugs(String url, QualityReport qualityReport) {
        url = url + "&types=BUG&severities=CRITICAL&status=OPEN";
        qualityReport.setCriticalBugs(getNumberOfIssues(url));
    }

    private void setBlockerBugs(String url, QualityReport qualityReport) {
        url = url + "&types=BUG&severities=BLOCKER&status=OPEN";
        qualityReport.setBlockerBugs(getNumberOfIssues(url));
    }

    private void setOtherVulnerabilities(String url, QualityReport qualityReport) {
        url = url + "&types=VULNERABILITY&severities=INFO&status=OPEN";
        qualityReport.setOtherVulnerabilities(getNumberOfIssues(url));
    }

    private void setMajorVulnerabilities(String url, QualityReport qualityReport) {
        url = url + "&types=VULNERABILITY&severities=MAJOR&status=OPEN";
        qualityReport.setMajorVulnerabilities(getNumberOfIssues(url));
    }

    private void setMinorVulnerabilities(String url, QualityReport qualityReport) {
        url = url + "&types=VULNERABILITY&severities=MINOR&status=OPEN";
        qualityReport.setMinorVulnerabilities(getNumberOfIssues(url));
    }

    private void setCriticalVulnerabilities(String url, QualityReport qualityReport) {
        url = url + "&types=VULNERABILITY&severities=CRITICAL&status=OPEN";
        qualityReport.setCriticalVulnerabilities(getNumberOfIssues(url));
    }

    private void setBlockerVulnerabilities(String url, QualityReport qualityReport) {
        url = url + "&types=VULNERABILITY&severities=BLOCKER&status=OPEN";
        qualityReport.setBlockerVulnerabilities(getNumberOfIssues(url));
    }


    private Integer getNumberOfIssues(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        DocumentContext jsonContext = JsonPath.parse(response.getBody());
        return jsonContext.read("$.total");
    }

}
