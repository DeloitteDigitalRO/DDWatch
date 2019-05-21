package com.deloitte.ddwatch.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "project_report")
@Data @Builder
public class MetricsReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "empire_time_value")
    private BigDecimal empireTimeValue;

    @Enumerated
    @Column(name = "empire_time_status")
    private Status empireTimeStatus;

    @Column(name = "delivery_value")
    private BigDecimal deliveryValue;

    @Enumerated
    @Column(name = "delivery_status")
    private Status deliveryStatus;

    @Column(name = "attrition_value")
    private BigDecimal attritionValue;

    @Enumerated
    @Column(name = "attrition_status")
    private Status attritionStatus;

    @Column(name = "invoicing_value")
    private String invoicingValue;

    @Enumerated
    @Column(name = "invoicing_status")
    private Status invoicingStatus;

    @Column(name = "escalation_seniority_value")
    private String escalationSeniorityValue;

    @Column(name = "escalation_root_cause_value")
    private String escalationRootCauseValue;

    @Enumerated
    @Column(name = "escalation_status")
    private Status escalationStatus;

    @Column(name = "change_order_value")
    private String changeOrderValue;

    @Enumerated
    @Column(name = "change_order_status")
    private Status changeOrderStatus;

    @Column(name = "test_coverage_value")
    private BigDecimal testCoverageValue;

    @Enumerated
    @Column(name = "test_coverage_status")
    private Status testCoverageStatus;

    @Column(name = "branch_coverage_value")
    private BigDecimal branchCoverageValue;

    @Enumerated
    @Column(name = "branch_coverage_status")
    private Status branchCoverageStatus;

    @Column(name = "duplication_density_value")
    private BigDecimal duplicationDensityValue;

    @Enumerated
    @Column(name = "duplication_density_status")
    private Status duplicationDensityStatus;

    @Column(name = "risk_overall_status")
    private Status riskOverallStatus;
}
