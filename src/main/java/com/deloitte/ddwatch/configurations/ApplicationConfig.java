package com.deloitte.ddwatch.configurations;

import com.deloitte.ddwatch.dtos.ProjectDTO;
import com.deloitte.ddwatch.dtos.QualityQuestionsAnswersDTO;
import com.deloitte.ddwatch.dtos.QualityReportDTO;
import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.model.Project;
import com.deloitte.ddwatch.model.QualityQuestionsAnswers;
import com.deloitte.ddwatch.model.QualityReport;
import com.deloitte.ddwatch.model.Tag;
import com.deloitte.ddwatch.model.json.QualityQuestions;
import com.deloitte.ddwatch.model.json.Question;
import com.google.gson.Gson;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
//    @Scope("prototype") //Todo Porxy enabled?
    public ModelMapper modelMapper(Map<String, Question> qualityQuestionsMap) {
        ModelMapper modelMapper = new ModelMapper();


        Converter<QualityQuestionsAnswers, QualityQuestionsAnswersDTO> questionTextConverter =  new Converter<QualityQuestionsAnswers, QualityQuestionsAnswersDTO>() {
            public QualityQuestionsAnswersDTO convert(MappingContext<QualityQuestionsAnswers, QualityQuestionsAnswersDTO> context) {
                QualityQuestionsAnswers s = context.getSource();
                QualityQuestionsAnswersDTO d = new QualityQuestionsAnswersDTO();
                d.setQuestionId(s.getQuestionId());
                d.setAnswer(s.getAnswer());
                d.setText(qualityQuestionsMap.get(s.getQuestionId()).getText());
                return d;
            }
        };

        Converter<QualityReportDTO, QualityReport> questionDTOConverter =  new Converter<QualityReportDTO, QualityReport>() {
            public QualityReport convert(MappingContext<QualityReportDTO, QualityReport> context) {
                QualityReportDTO s = context.getSource();
                QualityReport d = new QualityReport();

                for (QualityQuestionsAnswersDTO answerDTO : s.getQuestionsAnswers()) {
                    QualityQuestionsAnswers answer = new QualityQuestionsAnswers();
                    answer.setQuestionId(answerDTO.getQuestionId());
                    answer.setAnswer(answerDTO.getAnswer());
                    d.addQualityQuestionAnswer(answer);
                }

                return d;
            }
        };

        Converter<Set<String>, Set<Tag>> tagConverter2 = new Converter<Set<String>, Set<Tag>>() {
            public Set<Tag> convert(MappingContext<Set<String>, Set<Tag>> context) {
                Set<Tag> tags = new HashSet<>();
                for(String tagName : context.getSource()) {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    tags.add(tag);
                }
                return tags;
            }
        };

        Converter<Set<Tag>, Set<String>> tagConverter = context -> context.getSource() == null ? null :
                                    context.getSource().stream().map(Tag::getName).collect(Collectors.toSet());

        modelMapper.addMappings(new PropertyMap<Project, ProjectDTO>() {
            @Override
            protected void configure() {
                using(tagConverter).map(source.getTags(), destination.getTags());
            }
        });

        modelMapper.addMappings(new PropertyMap<ProjectDTO, Project>() {

            @Override
            protected void configure() {
                using(tagConverter2).map(source.getTags(), destination.getTags());
            }
        });

        modelMapper.addConverter(questionTextConverter);
        modelMapper.addConverter(questionDTOConverter);

        return modelMapper;
    }

    @Bean
    public ProjectMock projectMock() {
        return new ProjectMock();
    }

    @Bean
    public QualityQuestions qualityQuestions() {
        QualityQuestions qualityQuestions = null;
        try {
            String file = new String(Files.readAllBytes(ResourceUtils.getFile("classpath:quality-questions.json").toPath()));
            qualityQuestions = new Gson().fromJson(file, QualityQuestions.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return qualityQuestions;
    }

    @Bean
    public Map<String, Question> qualityQuestionsMap (QualityQuestions qualityQuestions) {
        Map<String, Question> qualityQuestionsMap = qualityQuestions.getQuestions().stream().collect(
                Collectors.toMap(Question::getId, Function.identity()));
        return qualityQuestionsMap;
    }
}
