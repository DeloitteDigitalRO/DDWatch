package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.model.DeliveryReport;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeliveryReportService {

    public DeliveryReport create(DeliveryReport deliveryReport) {
        deliveryReport.setUpdateDate(LocalDateTime.now());
        //additional processing;
        return deliveryReport;
    }
}
