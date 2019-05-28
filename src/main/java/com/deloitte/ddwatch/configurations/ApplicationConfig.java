package com.deloitte.ddwatch.configurations;

import com.deloitte.ddwatch.dtos.*;
import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.model.*;
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

        Converter<DeliveryReport, DeliveryReportDTO> deliveryReportConverter =  new Converter<DeliveryReport, DeliveryReportDTO>() {
            public DeliveryReportDTO convert(MappingContext<DeliveryReport, DeliveryReportDTO> context) {
                DeliveryReport s = context.getSource();
                DeliveryReportDTO d = new DeliveryReportDTO();
                d.setMetricsReport(modelMapper.map(s.getMetricsReport(), MetricsReportDTO.class));
                d.setProjectId(s.getProject().getId());
                d.setUpdateDate(s.getUpdateDate());
                return d;
            }
        };

        Converter<Set<String>, Set<Tag>> projectTagConverter = new Converter<Set<String>, Set<Tag>>() {
            public Set<Tag> convert(MappingContext<Set<String>, Set<Tag>> context) {
                return new HashSet<>();
            }
        };


        Converter<String, Tag> stringToTagConvertor = new Converter<String, Tag>() {
            public Tag convert(MappingContext<String, Tag> context) {
                String tagName = context.getSource();
                Tag tag = new Tag();
                tag.setName(tagName);
                return tag;
            }
        };


        Converter<Set<Tag>, Set<String>> tagConverter = context -> context.getSource() == null ? null :
                                    context.getSource().stream().map(Tag::getName).collect(Collectors.toSet());


        Converter<Status, String> statusStringConverter = new Converter<Status, String>() {
            public String convert(MappingContext<Status, String> context) {
                return context.getSource() == null ? null : context.getSource().getExcelCode();
            }
        };

        modelMapper.addMappings(new PropertyMap<Project, ProjectDTO>() {
            @Override
            protected void configure() {
                using(tagConverter).map(source.getTags(), destination.getTags());
            }
        });

        modelMapper.addMappings(new PropertyMap<ProjectDTO, Project>() {

            @Override
            protected void configure() {
                using(projectTagConverter).map(source.getTags(), destination.getTags());
            }
        });

        modelMapper.addConverter(questionTextConverter);
        modelMapper.addConverter(questionDTOConverter);
        modelMapper.addConverter(stringToTagConvertor);
        modelMapper.addConverter(statusStringConverter);
        modelMapper.addConverter(deliveryReportConverter);

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
