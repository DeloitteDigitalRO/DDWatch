package com.deloitte.ddwatch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class Project {

    @Id
    String id;
    String name;
    List<String> tags;

    String description;
    String deliveryLead;
    String deliveryLeadEmail;

    ProjectStatus deliveryStatus;
    ProjectStatus qualityStatus;

    Set<QualityReport> qualityReports;
    LocalDateTime lastQualityReport;

    Set<DeliveryReport> deliveryReports;
    LocalDateTime lastDeliveryReport;

    String sonarQubeUrl;
}
