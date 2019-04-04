package com.deloitte.ddwatch.services;

import com.deloitte.ddwatch.dtos.QualityQuestionsAnswersDTO;
import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.model.QualityQuestionsAnswers;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.model.json.QualityQuestions;
import com.deloitte.ddwatch.model.json.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Map;

@Service
public class QualityReportService {

    @Autowired
    private QualityQuestions qualityQuestions;
    @Autowired
    private Map<String, Question> qualityQuestionsMap;

    public QualityReport test(QualityReportDTO qualityReportDTO) {

        QualityReport qualityReport = new QualityReport();

        for(QualityQuestionsAnswersDTO answerDTO : qualityReportDTO.getQualityQuestionsAnswers()) {
            QualityQuestionsAnswers answer = new QualityQuestionsAnswers();
            answer.setQuestionId(answerDTO.getQuestionId());
            answer.setAnswer(answerDTO.getAnswer());
            qualityReport.addQualityQuestionAnswer(answer);
        }
        return qualityReport;
    }
}
