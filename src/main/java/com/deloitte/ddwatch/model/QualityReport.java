package com.deloitte.ddwatch.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "quality_report")
public class QualityReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne(mappedBy = "qualityReport", cascade = CascadeType.ALL)
    private SonarQubeReport sonarQubeReport;

    @OneToMany(mappedBy = "qualityReport", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @OrderBy(value = "updateDate DESC")
    private Set<QualityQuestionsAnswers> questionsAnswers = new HashSet<>();

    @Enumerated
    private Status qualityStatus;
    private LocalDateTime updateDate;

    public void addSonarQubeReport(SonarQubeReport sonarQubeReport) {
        setSonarQubeReport(sonarQubeReport);
        sonarQubeReport.setQualityReport(this);
    }

    public void addQualityQuestionAnswer(QualityQuestionsAnswers answer) {
        questionsAnswers.add(answer);
        answer.setQualityReport(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualityReport that = (QualityReport) o;
        return Objects.equals(getUpdateDate(), that.getUpdateDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUpdateDate());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
