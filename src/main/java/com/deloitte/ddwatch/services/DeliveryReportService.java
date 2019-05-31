package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.DeliveryReport;
import com.deloitte.ddwatch.model.MetricsReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.repositories.DeliveryReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DeliveryReportService {

    @Autowired
    MetricsReportService metricsReportService;

    @Autowired
    DeliveryReportRepository deliveryReportRepository;

    @Transactional
    public DeliveryReport createDeliveryReport(Project project, InputStream rawDeliveryReport) {
        MetricsReport metricsReport = metricsReportService.parseMetricsReport(rawDeliveryReport);

        DeliveryReport deliveryReport = new DeliveryReport();
        deliveryReport.setUpdateDate(LocalDateTime.now());
        deliveryReport.setProject(project);
        metricsReport.setDeliveryReport(deliveryReport);
        metricsReport.setCreatedOn(LocalDateTime.now());
        deliveryReport.setMetricsReport(metricsReport);
        return  deliveryReportRepository.save(deliveryReport);
    }

    public List<DeliveryReport> getDeliveryReports(Long projectId) {
        return deliveryReportRepository.findByProjectIdOrderByUpdateDateDesc(projectId);
    }
}
