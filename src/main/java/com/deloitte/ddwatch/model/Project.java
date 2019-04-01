package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.andreinc.mockneat.unit.objects.Filler.filler;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;
//    List<String> tags;

    String description;
    String deliveryLead;
    String deliveryLeadEmail;

    ProjectStatus deliveryStatus;
    ProjectStatus qualityStatus;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<QualityReport> qualityReports = new ArrayList<>();
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
}
