package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static void main(String[] args) {
        SonarQubeReport h1 = new SonarQubeReport();
        System.out.println(h1);
    }
}
