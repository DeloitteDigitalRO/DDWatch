package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "quality_questions_answers")
public class QualityQuestionsAnswers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "quality_report_id")
    private QualityReport qualityReport;

    private String questionId;
    private String answer;

}
