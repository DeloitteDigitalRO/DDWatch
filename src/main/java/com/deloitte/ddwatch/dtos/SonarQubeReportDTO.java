package com.deloitte.ddwatch.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SonarQubeReportDTO implements Serializable {
    String name;
    String key;

    Integer totalBugs;
    Integer blockerBugs;
    Integer criticalBugs;
    Integer majorBugs;
    Integer minorBugs;
    Integer otherBugs;

    Integer linesOfCode;
    String defectDensity; //no of bugs/no of lines

    Integer totalIssues; //violations

    Integer totalVulnerabilities;
    Integer blockerVulnerabilities;
    Integer criticalVulnerabilities;
    Integer majorVulnerabilities;
    Integer minorVulnerabilities;
    Integer otherVulnerabilities;

    Integer totalCodeSmells;
    Integer blockerCodeSmells;
    Integer criticalCodeSmells;
    Integer majorCodeSmells;
    Integer minorCodeSmells;
    Integer otherCodeSmells;

    Integer duplicatedLines;
    Double duplicatedLinesDensity;
    Integer duplicatedBlocks;

    Integer cyclomaticComplexity;
    Integer cognitiveComplexity;

    Double overallCoverage;
    Double lineCoverage;
    Double conditionsCoverage;

}
