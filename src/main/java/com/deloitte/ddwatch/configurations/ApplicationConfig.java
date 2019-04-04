package com.deloitte.ddwatch.configurations;

import com.deloitte.ddwatch.mockunit.ProjectMock;
import com.deloitte.ddwatch.model.json.QualityQuestions;
import com.deloitte.ddwatch.model.json.Question;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.MapsId;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Scope("prototype")
    public ModelMapper modelMapper() {
        return new ModelMapper();
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
