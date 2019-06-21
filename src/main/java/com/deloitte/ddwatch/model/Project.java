package com.deloitte.ddwatch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "name", nullable = false, length=64, unique = true)
    private String name;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "delivery_lead", nullable=false, length = 64)
    private String deliveryLead;

    @Column(name = "delivery_lead_email", nullable = false, length = 128)
    private String deliveryLeadEmail;

    @Column(name = "tech_lead", nullable = false, length = 64)
    private String technicalLead;

    @Column(name = "tech_lead_email", nullable = false, length = 128)
    private String technicalLeadEmail;

    private Status deliveryStatus;

    private Status qualityStatus;

    @Column(name = "last_quality_report")
    private LocalDateTime lastQualityReport;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy(value = "updateDate DESC")
    private List<DeliveryReport> deliveryReports = new ArrayList<>();

    @Column(name = "last_delivery_report")
    private LocalDateTime lastDeliveryReport;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "project_tag",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProjectRepo> projectRepos = new HashSet<>();

    public void addProjectRepo(ProjectRepo projectRepo) {
        projectRepos.add(projectRepo);
        projectRepo.setProject(this);
    }

    public void addDeliveryReport(DeliveryReport deliveryReport) {
        deliveryReports.add(deliveryReport);
        deliveryReport.setProject(this);
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
                Objects.equals(getLastDeliveryReport(), project.getLastDeliveryReport());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getDeliveryLead(), getDeliveryLeadEmail(), getTechnicalLead(), getTechnicalLeadEmail(), getDeliveryStatus(), getQualityStatus(), getLastQualityReport(), getLastDeliveryReport());
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deliveryLead='" + deliveryLead + '\'' +
                ", deliveryLeadEmail='" + deliveryLeadEmail + '\'' +
                ", technicalLead='" + technicalLead + '\'' +
                ", technicalLeadEmail='" + technicalLeadEmail + '\'' +
                ", deliveryStatus=" + deliveryStatus +
                ", qualityStatus=" + qualityStatus +
                ", lastQualityReport=" + lastQualityReport +
                ", deliveryReports=" + deliveryReports +
                ", lastDeliveryReport=" + lastDeliveryReport +
                ", tags=" + tags +
                ", projectRepos=" + projectRepos +
                '}';
    }
}
