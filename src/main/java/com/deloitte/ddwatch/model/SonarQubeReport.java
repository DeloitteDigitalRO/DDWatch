package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "sonar_qube_report")
public class SonarQubeReport {

    @Id
    Long id;

    @OneToOne
    @MapsId
    private QualityReport qualityReport;

    String name;
    String key;

    Integer totalBugs;
    Integer blockerBugs;
    Integer criticalBugs;
    Integer majorBugs;
    Integer minorBugs;
    Integer otherBugs;

    Integer linesOfCode;
    Double defectDensity; //no of bugs/no of lines

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
