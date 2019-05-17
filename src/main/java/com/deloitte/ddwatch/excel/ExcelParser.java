package com.deloitte.ddwatch.excel;

import com.deloitte.ddwatch.exceptions.ProcessingException;
import com.deloitte.ddwatch.model.ProjectMetricsReport;
import com.deloitte.ddwatch.model.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class ExcelParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${excel.metrics_config_file}")
    private String metricsConfigFile;

    public ProjectMetricsReport parseMetrics(MultipartFile rawExcel) {
        Workbook workbook = getWorkbook(rawExcel);

        MetricsExcelConfig config = getMetricsExcelConfig();

        Sheet sheet= Optional.ofNullable(workbook.getSheet(config.getSheet()))
                .orElseThrow(() -> new ProcessingException("Could not open Metrics sheet."));

        return ProjectMetricsReport.builder()
                .empireTimeStatus(getMetricStatus(sheet, config.getEmpireTime()))
                .deliveryStatus(getMetricStatus(sheet, config.getDelivery()))
                .attritionStatus(getMetricStatus(sheet, config.getAttrition()))
                .invoicingStatus(getMetricStatus(sheet, config.getInvoicing()))
                .escalationStatus(getMetricStatus(sheet, config.getEscalation()))
                .changeOrderStatus(getMetricStatus(sheet, config.getChangeOrder()))
                .testCoverageStatus(getMetricStatus(sheet, config.getTestCodeCoverage()))
                .branchCoverageStatus(getMetricStatus(sheet, config.getBranchCoverage()))
                .duplicationDensityStatus(getMetricStatus(sheet, config.getDuplicationDensity()))
                .riskOverallStatus(getMetricStatus(sheet, config.getRiskOverallStatus()))
                .build();
    }

    private Workbook getWorkbook(MultipartFile rawExcel) {
        try {
            return new XSSFWorkbook(rawExcel.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProcessingException("Could not open project file.");
        }
    }

    private String getCellText(Sheet sheet, CellPosition position) {
        Row row = Optional.ofNullable(sheet.getRow(position.getRow()))
                .orElseThrow(() -> new ProcessingException(String.format("Could not find row %d",
                        position.getRow())));

        Cell cell = Optional.ofNullable(row.getCell(position.getCol()))
                .orElseThrow(() -> new ProcessingException(String.format("Could not find cell %d on row %d",
                        position.getCol(), position.getRow())));

        String text = cell.getStringCellValue();
        log.info("Get cell text {} {}", position, text);
        return text;
    }

    private MetricsExcelConfig getMetricsExcelConfig() {
        try {
            return objectMapper.readValue(new File(metricsConfigFile), MetricsExcelConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProcessingException("Could not open metrics config file.");
        }
    }

    private Status getMetricStatus(Sheet sheet, CellPosition position) {
        return Status.getStatusByExcelCode(getCellText(sheet, position));
    }
}
