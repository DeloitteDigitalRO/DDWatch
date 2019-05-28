package com.deloitte.ddwatch.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricsReportDTO {

    private Long id;

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

    private Long deliveryReportId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MetricsReportDTO reportDTO = (MetricsReportDTO) o;

        return new EqualsBuilder()
                .append(empireTimeValue, reportDTO.empireTimeValue)
                .append(empireTimeStatus, reportDTO.empireTimeStatus)
                .append(deliveryValue, reportDTO.deliveryValue)
                .append(deliveryStatus, reportDTO.deliveryStatus)
                .append(attritionValue, reportDTO.attritionValue)
                .append(attritionStatus, reportDTO.attritionStatus)
                .append(invoicingValue, reportDTO.invoicingValue)
                .append(invoicingStatus, reportDTO.invoicingStatus)
                .append(escalationSeniorityValue, reportDTO.escalationSeniorityValue)
                .append(escalationRootCauseValue, reportDTO.escalationRootCauseValue)
                .append(escalationStatus, reportDTO.escalationStatus)
                .append(changeOrderValue, reportDTO.changeOrderValue)
                .append(changeOrderStatus, reportDTO.changeOrderStatus)
                .append(testCoverageValue, reportDTO.testCoverageValue)
                .append(testCoverageStatus, reportDTO.testCoverageStatus)
                .append(branchCoverageValue, reportDTO.branchCoverageValue)
                .append(branchCoverageStatus, reportDTO.branchCoverageStatus)
                .append(duplicationDensityValue, reportDTO.duplicationDensityValue)
                .append(duplicationDensityStatus, reportDTO.duplicationDensityStatus)
                .append(riskOverallValue, reportDTO.riskOverallValue)
                .append(riskOverallStatus, reportDTO.riskOverallStatus)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(empireTimeValue)
                .append(empireTimeStatus)
                .append(deliveryValue)
                .append(deliveryStatus)
                .append(attritionValue)
                .append(attritionStatus)
                .append(invoicingValue)
                .append(invoicingStatus)
                .append(escalationSeniorityValue)
                .append(escalationRootCauseValue)
                .append(escalationStatus)
                .append(changeOrderValue)
                .append(changeOrderStatus)
                .append(testCoverageValue)
                .append(testCoverageStatus)
                .append(branchCoverageValue)
                .append(branchCoverageStatus)
                .append(duplicationDensityValue)
                .append(duplicationDensityStatus)
                .append(riskOverallValue)
                .append(riskOverallStatus)
                .toHashCode();
    }
}
