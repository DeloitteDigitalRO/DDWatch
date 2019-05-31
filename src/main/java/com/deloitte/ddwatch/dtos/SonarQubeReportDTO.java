package com.deloitte.ddwatch.dtos;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SonarQubeReportDTO implements Serializable {

    @NotEmpty
    String name;

    @NotEmpty
    String key;

    @NotNull
    @Min(0)
    Integer totalBugs;

    @Min(0)
    @NotNull
    Integer blockerBugs;

    @NotNull
    @Min(0)
    Integer criticalBugs;

    @NotNull
    @Min(0)
    Integer majorBugs;

    @NotNull
    @Min(0)
    Integer minorBugs;

    @NotNull
    @Min(0)
    Integer otherBugs;

    @NotNull
    @Min(0)
    Integer linesOfCode;

    @NotNull
    @Min(0)
    Double defectDensity; //no of bugs/no of lines

    @NotNull
    @Min(0)
    Integer totalIssues; //violations

    @NotNull
    @Min(0)
    Integer totalVulnerabilities;

    @NotNull
    @Min(0)
    Integer blockerVulnerabilities;

    @NotNull
    @Min(0)
    Integer criticalVulnerabilities;

    @NotNull
    @Min(0)
    Integer majorVulnerabilities;

    @NotNull
    @Min(0)
    Integer minorVulnerabilities;

    @NotNull
    @Min(0)
    Integer otherVulnerabilities;

    @NotNull
    @Min(0)
    Integer totalCodeSmells;

    @NotNull
    @Min(0)
    Integer blockerCodeSmells;

    @NotNull
    @Min(0)
    Integer criticalCodeSmells;

    @NotNull
    @Min(0)
    Integer majorCodeSmells;

    @NotNull
    @Min(0)
    Integer minorCodeSmells;

    @NotNull
    @Min(0)
    Integer otherCodeSmells;

    @NotNull
    @Min(0)
    Integer duplicatedLines;

    @NotNull
    @Min(0)
    Double duplicatedLinesDensity;

    @NotNull
    @Min(0)
    Integer duplicatedBlocks;

    Integer cyclomaticComplexity;
    Integer cognitiveComplexity;

    Double overallCoverage;
    Double lineCoverage;
    Double conditionsCoverage;

}
