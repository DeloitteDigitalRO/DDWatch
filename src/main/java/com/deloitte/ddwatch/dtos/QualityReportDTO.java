package com.deloitte.ddwatch.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class QualityReportDTO implements Serializable {

    private SonarQubeReportDTO sonarQubeReport;
    private LocalDateTime updateDate;

}
