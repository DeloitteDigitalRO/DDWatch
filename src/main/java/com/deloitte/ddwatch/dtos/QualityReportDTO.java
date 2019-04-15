package com.deloitte.ddwatch.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class QualityReportDTO implements Serializable {

    private SonarQubeReportDTO sonarQubeReport;
    private String qualityStatus;
    private LocalDateTime updateDate;
    private List<QualityQuestionsAnswersDTO> questionsAnswers;

}
