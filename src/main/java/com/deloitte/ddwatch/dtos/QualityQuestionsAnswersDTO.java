package com.deloitte.ddwatch.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class QualityQuestionsAnswersDTO implements Serializable {

    private String questionId;
    private String text;
    private String answer;
}
