package com.deloitte.ddwatch.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.andreinc.jbvext.annotations.misc.OneOfStrings;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class QualityReportDTO implements Serializable {

    @Valid
    private SonarQubeReportDTO sonarQubeReport;

    @OneOfStrings({"RED", "GREEN", "AMBER"})
    private String qualityStatus;

    private LocalDateTime updateDate;

    private List<@Valid QualityQuestionsAnswersDTO> questionsAnswers;

}
