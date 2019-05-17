package com.deloitte.ddwatch.excel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class MetricsExcelConfig extends ExcelConfig{
    private CellPosition empireTime;

    private CellPosition delivery;

    private CellPosition attrition;

    private CellPosition invoicing;

    private CellPosition escalation;

    private CellPosition changeOrder;

    private CellPosition testCodeCoverage;

    private CellPosition branchCoverage;

    private CellPosition duplicationDensity;

    public CellPosition riskOverallStatus;
}
