package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "QualityReport")
public class QualityReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

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
