package com.deloitte.ddwatch.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryReportDTO implements Serializable {

    private LocalDateTime updateDate;

    private Long projectId;

    private MetricsReportDTO metricsReportDTO;
}
