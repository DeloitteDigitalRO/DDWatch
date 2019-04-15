package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.DeliveryReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryReportService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryReport.class.getCanonicalName());

    public DeliveryReport create(DeliveryReport deliveryReport) {
        deliveryReport.setUpdateDate(LocalDateTime.now());
        //additional processing;
        return deliveryReport;
    }
}
