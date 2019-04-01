package com.deloitte.ddwatch.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class QualityReportDTO implements Serializable {
    String name;
    String key;

    Integer totalBugs;
    Integer blockerBugs;
    Integer criticalBugs;
    Integer majorBugs;
    Integer otherBugs;

    Integer linesOfCode;
    Integer defectDensity; //no of bugs/no of lines

    Integer totalIssues; //violations

    Integer totalVulnerabilities;
    Integer blockers;
    Integer criticals;
    Integer majors;
    Integer others;

    Integer codeSmels;
    Integer duplicatedLines;
    Float duplicatedLinesDensity;
    Integer duplicatedBlocks;

    Integer cyclomaticComplexity;
    Integer cognitiveComplexity;

    Float overallCoverage;
    Float lineCoverage;
    Float conditionsCoverage;

}
