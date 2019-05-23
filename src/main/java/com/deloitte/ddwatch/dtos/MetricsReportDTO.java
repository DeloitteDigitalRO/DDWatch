package com.deloitte.ddwatch.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricsReportDTO {
    private Long id;

    private Long projectId;

    private Double empireTimeValue;

    private String empireTimeStatus;

    private Double deliveryValue;

    private String deliveryStatus;

    private Double attritionValue;

    private String attritionStatus;

    private String invoicingValue;

    private String invoicingStatus;

    private String escalationSeniorityValue;

    private String escalationRootCauseValue;

    private String escalationStatus;

    private String changeOrderValue;

    private String changeOrderStatus;

    private Double testCoverageValue;

    private String testCoverageStatus;

    private Double branchCoverageValue;

    private String branchCoverageStatus;

    private Double duplicationDensityValue;

    private String duplicationDensityStatus;

    private String riskOverallValue;

    private String riskOverallStatus;

    private LocalDateTime createdOn;
}
