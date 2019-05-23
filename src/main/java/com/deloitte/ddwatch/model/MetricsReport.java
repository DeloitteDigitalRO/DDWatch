package com.deloitte.ddwatch.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "metrics_report")
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

    @Column(name = "risk_overall_value")
    private String riskOverallValue;

    @Column(name = "risk_overall_status")
    private Status riskOverallStatus;

    @Column(name = "created_on")
    private Date createdOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetricsReport that = (MetricsReport) o;
        return Objects.equals(empireTimeValue, that.empireTimeValue) &&
                empireTimeStatus == that.empireTimeStatus &&
                Objects.equals(deliveryValue, that.deliveryValue) &&
                deliveryStatus == that.deliveryStatus &&
                Objects.equals(attritionValue, that.attritionValue) &&
                attritionStatus == that.attritionStatus &&
                Objects.equals(invoicingValue, that.invoicingValue) &&
                invoicingStatus == that.invoicingStatus &&
                Objects.equals(escalationSeniorityValue, that.escalationSeniorityValue) &&
                Objects.equals(escalationRootCauseValue, that.escalationRootCauseValue) &&
                escalationStatus == that.escalationStatus &&
                Objects.equals(changeOrderValue, that.changeOrderValue) &&
                changeOrderStatus == that.changeOrderStatus &&
                Objects.equals(testCoverageValue, that.testCoverageValue) &&
                testCoverageStatus == that.testCoverageStatus &&
                Objects.equals(branchCoverageValue, that.branchCoverageValue) &&
                branchCoverageStatus == that.branchCoverageStatus &&
                Objects.equals(duplicationDensityValue, that.duplicationDensityValue) &&
                duplicationDensityStatus == that.duplicationDensityStatus &&
                Objects.equals(riskOverallValue, that.riskOverallValue) &&
                riskOverallStatus == that.riskOverallStatus &&
                Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empireTimeValue, empireTimeStatus, deliveryValue, deliveryStatus, attritionValue, attritionStatus, invoicingValue, invoicingStatus, escalationSeniorityValue, escalationRootCauseValue, escalationStatus, changeOrderValue, changeOrderStatus, testCoverageValue, testCoverageStatus, branchCoverageValue, branchCoverageStatus, duplicationDensityValue, duplicationDensityStatus, riskOverallValue, riskOverallStatus, createdOn);
    }

    @Override
    public String toString() {
        return "MetricsReport{" +
                "empireTimeValue=" + empireTimeValue +
                ", empireTimeStatus=" + empireTimeStatus +
                ", deliveryValue=" + deliveryValue +
                ", deliveryStatus=" + deliveryStatus +
                ", attritionValue=" + attritionValue +
                ", attritionStatus=" + attritionStatus +
                ", invoicingValue='" + invoicingValue + '\'' +
                ", invoicingStatus=" + invoicingStatus +
                ", escalationSeniorityValue='" + escalationSeniorityValue + '\'' +
                ", escalationRootCauseValue='" + escalationRootCauseValue + '\'' +
                ", escalationStatus=" + escalationStatus +
                ", changeOrderValue='" + changeOrderValue + '\'' +
                ", changeOrderStatus=" + changeOrderStatus +
                ", testCoverageValue=" + testCoverageValue +
                ", testCoverageStatus=" + testCoverageStatus +
                ", branchCoverageValue=" + branchCoverageValue +
                ", branchCoverageStatus=" + branchCoverageStatus +
                ", duplicationDensityValue=" + duplicationDensityValue +
                ", duplicationDensityStatus=" + duplicationDensityStatus +
                ", riskOverallValue='" + riskOverallValue + '\'' +
                ", riskOverallStatus=" + riskOverallStatus +
                ", createdOn=" + createdOn +
                '}';
    }
}
