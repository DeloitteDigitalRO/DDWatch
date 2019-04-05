package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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
//    @OrderBy(value = "updateDate DESC")
    Set<QualityReport> qualityReports = new HashSet<>();

    @Column(name = "last_quality_report")
    LocalDateTime lastQualityReport;

    @OneToMany(mappedBy = "project")
    List<DeliveryReport> deliveryReports;

    @Column(name = "last_delivery_report")
    LocalDateTime lastDeliveryReport;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "project_tag",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @Column(name = "sonarqube_url", length = 1024)
    String sonarQubeUrl;

    @Column(name = "sonarqube_component_url", length = 256)
    String sonarComponentKey;

    public void addQualityReport(QualityReport qualityReport) {
        qualityReports.add(qualityReport);
        qualityReport.setProject(this);
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getProjects().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getProjects().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(getName(), project.getName()) &&
                Objects.equals(getDescription(), project.getDescription()) &&
                Objects.equals(getDeliveryLead(), project.getDeliveryLead()) &&
                Objects.equals(getDeliveryLeadEmail(), project.getDeliveryLeadEmail()) &&
                Objects.equals(getTechnicalLead(), project.getTechnicalLead()) &&
                Objects.equals(getTechnicalLeadEmail(), project.getTechnicalLeadEmail()) &&
                getDeliveryStatus() == project.getDeliveryStatus() &&
                getQualityStatus() == project.getQualityStatus() &&
                Objects.equals(getLastQualityReport(), project.getLastQualityReport()) &&
                Objects.equals(getLastDeliveryReport(), project.getLastDeliveryReport()) &&
                Objects.equals(getSonarQubeUrl(), project.getSonarQubeUrl()) &&
                Objects.equals(getSonarComponentKey(), project.getSonarComponentKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getDeliveryLead(), getDeliveryLeadEmail(), getTechnicalLead(), getTechnicalLeadEmail(), getDeliveryStatus(), getQualityStatus(), getLastQualityReport(), getLastDeliveryReport(), getSonarQubeUrl(), getSonarComponentKey());
    }
}
