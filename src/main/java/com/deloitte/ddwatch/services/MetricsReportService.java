package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.excel.ExcelParser;
import com.deloitte.ddwatch.model.MetricsReport;
import com.deloitte.ddwatch.repositories.MetricsReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
public class MetricsReportService {
    @Autowired
    private MetricsReportRepository metricsReportRepository;

    @Autowired
    private ExcelParser excelParser;

    public MetricsReport parseMetricsReport(InputStream rawReportFile) {
        return excelParser.parseMetrics(rawReportFile);
    }
}
