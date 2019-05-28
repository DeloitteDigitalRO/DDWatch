package com.deloitte.ddwatch.excel;

import com.deloitte.ddwatch.exceptions.ProcessingException;
import com.deloitte.ddwatch.model.MetricsReport;
import com.deloitte.ddwatch.model.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Utility class used to parse information stored in project xls.
 */
@Slf4j
@Component
public class ExcelParser {
    private final String CELL_TYPE_STRING = "text";
    private final String CELL_TYPE_NUMERIC = "numeric";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Path to a JSON file that contains the xls mappings for the relevant project metrics.
     */
    @Value("${excel.metrics_config_file}")
    private String metricsConfigFile;

    public MetricsReport parseMetrics(MultipartFile rawExcel) {
        Workbook workbook = getWorkbook(rawExcel);

        MetricsExcelConfig config = getMetricsExcelConfig();

        Sheet sheet= Optional.ofNullable(workbook.getSheet(config.getSheet()))
                .orElseThrow(() -> new ProcessingException("Could not open Metrics sheet."));

        return MetricsReport.builder()
                .empireTimeValue(getNumericValue(sheet, config.getEmpireTimeValue()))
                .deliveryValue(getNumericValue(sheet, config.getDeliveryValue()))
                .attritionValue(getNumericValue(sheet, config.getAttritionValue()))
                .invoicingValue(getStringValue(sheet, config.getInvoicingValue()))
                .escalationSeniorityValue(getStringValue(sheet, config.getEscalationSeniorityValue()))
                .escalationRootCauseValue(getStringValue(sheet, config.getEscalationRootCauseValue()))
                .changeOrderValue(getStringValue(sheet, config.getChangeOrderValue()))
                .testCoverageValue(getNumericValue(sheet, config.getTestCodeCoverageValue()))
                .branchCoverageValue(getNumericValue(sheet, config.getBranchCoverageValue()))
                .duplicationDensityValue(getNumericValue(sheet, config.getDuplicationDensityValue()))
                .empireTimeStatus(getMetricStatus(sheet, config.getEmpireTimeStatus()))
                .deliveryStatus(getMetricStatus(sheet, config.getDeliveryStatus()))
                .attritionStatus(getMetricStatus(sheet, config.getAttritionStatus()))
                .invoicingStatus(getMetricStatus(sheet, config.getInvoicingStatus()))
                .escalationStatus(getMetricStatus(sheet, config.getEscalationStatus()))
                .changeOrderStatus(getMetricStatus(sheet, config.getChangeOrderStatus()))
                .testCoverageStatus(getMetricStatus(sheet, config.getTestCodeCoverageStatus()))
                .branchCoverageStatus(getMetricStatus(sheet, config.getBranchCoverageStatus()))
                .duplicationDensityStatus(getMetricStatus(sheet, config.getDuplicationDensityStatus()))
                .riskOverallValue(getStringValue(sheet, config.getRiskOverallValue()))
                .riskOverallStatus(getMetricStatus(sheet, config.getRiskOverallStatus()))
                .build();
    }

    private MetricsExcelConfig getMetricsExcelConfig() {
        try {
            return objectMapper.readValue(new File(metricsConfigFile), MetricsExcelConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProcessingException("Could not open metrics config file.");
        }
    }

    private Workbook getWorkbook(MultipartFile rawExcel) {
        try {
            return new XSSFWorkbook(rawExcel.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProcessingException("Could not open project file.");
        }
    }

    private Object getCellValue(Sheet sheet, ExcelCell excelCell) {
        Row row = Optional.ofNullable(sheet.getRow(excelCell.getRow()))
                .orElseThrow(() -> new ProcessingException(String.format("Could not find row %d",
                        excelCell.getRow())));

        Cell cell = Optional.ofNullable(row.getCell(excelCell.getCol()))
                .orElseThrow(() -> new ProcessingException(String.format("Could not find cell %d on row %d",
                        excelCell.getCol(), excelCell.getRow())));

        Object cellValue = null;
        try {
            switch (excelCell.getType()) {
                case CELL_TYPE_NUMERIC:
                    cellValue = cell.getNumericCellValue();
                    break;
                case CELL_TYPE_STRING:
                    cellValue = cell.getStringCellValue();
                    break;
            }
        } catch (IllegalStateException exception) {
            log.error("{} {}", exception.getMessage(), excelCell);
            throw new ProcessingException(String.format("Invalid type found on row %d cell %d. Expected %s",
                    excelCell.getRow(), excelCell.getCol(), excelCell.getType()));
        }

        log.info("Get cell value {} {}", excelCell, cellValue);
        return cellValue;
    }

    private Status getMetricStatus(Sheet sheet, ExcelCell position) {
        return Status.getStatusByExcelCode((String) getCellValue(sheet, position));
    }

    private Double getNumericValue(Sheet sheet, ExcelCell position) {
        return (Double) getCellValue(sheet, position);
    }

    private String getStringValue(Sheet sheet, ExcelCell position) {
        return (String) getCellValue(sheet, position);
    }
}
