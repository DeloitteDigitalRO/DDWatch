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
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "name", nullable = false, length=64, unique = true)
    String name;

    @Column(name = "description", length = 1024)
    String description;

    @Column(name = "delivery_lead", nullable=false, length = 64)
    String deliveryLead;

    @Column(name = "delivery_lead_email", nullable = false, length = 128)
    String deliveryLeadEmail;

    @Column(name = "tech_lead", nullable = false, length = 64)
    String technicalLead;

    @Column(name = "tech_lead_email", nullable = false, length = 128)
    String technicalLeadEmail;

    ProjectStatus deliveryStatus;
    ProjectStatus qualityStatus;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy(value = "updateDate DESC")
    List<SonarQubeReport> sonarQubeReports = new ArrayList<>();

    @Column(name = "last_quality_report")
    LocalDateTime lastQualityReport;

    @OneToMany(mappedBy = "project")
    List<DeliveryReport> deliveryReports;

    @Column(name = "last_delivery_report")
    LocalDateTime lastDeliveryReport;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "project_tag",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    @Column(name = "sonarqube_url", length = 1024)
    String sonarQubeUrl;

    @Column(name = "sonarqube_component_url", length = 256)
    String sonarComponentKey;

    public void addQualityReport(SonarQubeReport sonarQubeReport) {
        sonarQubeReports.add(sonarQubeReport);
        sonarQubeReport.setProject(this);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getProjects().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getProjects().remove(this);
    }
}
