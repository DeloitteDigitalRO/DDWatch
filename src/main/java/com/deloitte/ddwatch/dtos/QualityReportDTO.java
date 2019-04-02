package com.deloitte.ddwatch.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class QualityReportDTO implements Serializable {
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

    Integer codeSmels;
    Integer duplicatedLines;
    Float duplicatedLinesDensity;
    Integer duplicatedBlocks;

    Integer cyclomaticComplexity;
    Integer cognitiveComplexity;

    Float overallCoverage;
    Float lineCoverage;
    Float conditionsCoverage;

    LocalDateTime updateDate;
}
