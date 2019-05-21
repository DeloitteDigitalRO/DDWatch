package com.deloitte.ddwatch.dtos;

import com.deloitte.ddwatch.model.Status;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class MetricsReportDTO {
    public MetricsReportDTO() {}

    private Long id;

    private Long projectId;

    private BigDecimal empireTimeValue;

    private String empireTimeStatus;

    private BigDecimal deliveryValue;

    private String deliveryStatus;

    private BigDecimal attritionValue;

    private String attritionStatus;

    private String invoicingValue;

    private Status invoicingStatus;

    private String escalationSeniorityValue;

    private String escalationRootCauseValue;

    private String escalationStatus;

    private String changeOrderValue;

    private String changeOrderStatus;

    private BigDecimal testCoverageValue;

    private String testCoverageStatus;

    private String branchCoverageValue;

    private String branchCoverageStatus;

    private BigDecimal duplicationDensityValue;

    private String duplicationDensityStatus;

    private String riskOverallStatus;
}
