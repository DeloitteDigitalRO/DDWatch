package com.deloitte.ddwatch.dtos;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DeliveryReportDTO implements Serializable {

    private String field1;
    private String field2;
    private String field3;

    private LocalDateTime updateDate;

}
