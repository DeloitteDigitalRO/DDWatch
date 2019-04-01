package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static net.andreinc.mockneat.unit.objects.Filler.filler;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Project")
@Cacheable(false)
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
    List<QualityReport> qualityReports;
    LocalDateTime lastQualityReport;

    @OneToMany(mappedBy = "project")
    List<DeliveryReport> deliveryReports;
    LocalDateTime lastDeliveryReport;

    String sonarQubeUrl;
}
