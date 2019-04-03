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
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;

    String description;
    String deliveryLead;
    String deliveryLeadEmail;

    ProjectStatus deliveryStatus;
    ProjectStatus qualityStatus;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy(value = "updateDate DESC")
    List<SonarQubeReport> sonarQubeReports = new ArrayList<>();
    LocalDateTime lastQualityReport;

    @OneToMany(mappedBy = "project")
    List<DeliveryReport> deliveryReports;
    LocalDateTime lastDeliveryReport;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "project_tag",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    String sonarQubeUrl;
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
