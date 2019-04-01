package com.deloitte.ddwatch.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ProjectDTO {
    String id;
    String name;
    List<String> tags;

    String description;
    String deliveryLead;
    String deliveryLeadEmail;

    String deliveryStatus;
    String qualityStatus;

    Set<QualityReportDTO> qualityReports;
    LocalDateTime lastQualityReport;

    Set<DeliveryReportDTO> deliveryReports;
    LocalDateTime lastDeliveryReport;

    String sonarQubeUrl;
}
