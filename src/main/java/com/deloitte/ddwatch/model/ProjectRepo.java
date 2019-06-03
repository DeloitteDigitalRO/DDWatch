package com.deloitte.ddwatch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project_repo")
public class ProjectRepo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_default")
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "sonarqube_url", length = 1024)
    private String sonarQubeUrl;

    @Column(name = "sonarqube_component_url", length = 256)
    private String sonarComponentKey;

    @OneToMany(mappedBy = "projectRepo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<QualityReport> qualityReports = new HashSet<>();

    public void addQualityReport(QualityReport qualityReport) {
        qualityReports.add(qualityReport);
        qualityReport.setProjectRepo(this);
    }

}
