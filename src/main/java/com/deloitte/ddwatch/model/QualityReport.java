package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "quality_report")
public class QualityReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne(mappedBy = "qualityReport", cascade = CascadeType.ALL)
    private SonarQubeReport sonarQubeReport;

    private LocalDateTime updateDate;



    public void addSonarQubeReport(SonarQubeReport sonarQubeReport) {
        setSonarQubeReport(sonarQubeReport);
        sonarQubeReport.setQualityReport(this);
    }
}
