package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.DeliveryReport;
import com.deloitte.ddwatch.model.MetricsReport;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.repositories.DeliveryReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DeliveryReportService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryReport.class.getCanonicalName());

    @Autowired
    DeliveryReportRepository deliveryReportRepository;

    @Transactional
    public DeliveryReport createDeliveryReport(Project project, MetricsReport metricsReport) {
        DeliveryReport deliveryReport = new DeliveryReport();
        deliveryReport.setUpdateDate(LocalDateTime.now());
        deliveryReport.setProject(project);
        metricsReport.setDeliveryReport(deliveryReport);
        metricsReport.setCreatedOn(LocalDateTime.now());
        deliveryReport.setMetricsReport(metricsReport);
        return  deliveryReportRepository.save(deliveryReport);
    }
}
