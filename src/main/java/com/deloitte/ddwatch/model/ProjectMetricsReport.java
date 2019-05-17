package com.deloitte.ddwatch.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "project_report")
@Data @Builder
public class ProjectMetricsReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated
    @Column(name = "empire_time_status")
    private Status empireTimeStatus;

    @Enumerated
    @Column(name = "delivery_status")
    private Status deliveryStatus;

    @Enumerated
    @Column(name = "attrition_status")
    private Status attritionStatus;

    @Enumerated
    @Column(name = "invoicing_status")
    private Status invoicingStatus;

    @Enumerated
    @Column(name = "escalation_status")
    private Status escalationStatus;

    @Enumerated
    @Column(name = "change_order_status")
    private Status changeOrderStatus;

    @Enumerated
    @Column(name = "test_coverage_status")
    private Status testCoverageStatus;

    @Enumerated
    @Column(name = "branch_coverage_status")
    private Status branchCoverageStatus;

    @Enumerated
    @Column(name = "duplication_density_status")
    private Status duplicationDensityStatus;

    @Column(name = "risk_overall_status")
    private Status riskOverallStatus;
}
