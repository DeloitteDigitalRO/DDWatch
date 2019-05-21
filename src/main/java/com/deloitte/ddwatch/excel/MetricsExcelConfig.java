package com.deloitte.ddwatch.excel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class MetricsExcelConfig extends ExcelConfig {
    private ExcelCell empireTimeValue;

    private ExcelCell deliveryValue;

    private ExcelCell attritionValue;

    private ExcelCell invoicingValue;

    private ExcelCell escalationSeniorityValue;

    private ExcelCell escalationRootCauseValue;

    private ExcelCell changeOrderValue;

    private ExcelCell testCodeCoverageValue;

    private ExcelCell branchCoverageValue;

    private ExcelCell duplicationDensityValue;

    private ExcelCell empireTimeStatus;

    private ExcelCell deliveryStatus;

    private ExcelCell attritionStatus;

    private ExcelCell invoicingStatus;

    private ExcelCell escalationStatus;

    private ExcelCell changeOrderStatus;

    private ExcelCell testCodeCoverageStatus;

    private ExcelCell branchCoverageStatus;

    private ExcelCell duplicationDensityStatus;

    public ExcelCell riskOverallStatus;
}
