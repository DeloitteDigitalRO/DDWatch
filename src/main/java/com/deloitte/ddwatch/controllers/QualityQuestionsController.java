package com.deloitte.ddwatch.controllers;

import com.deloitte.ddwatch.model.json.QualityQuestions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions")
@CrossOrigin
public class QualityQuestionsController {

    private static final Logger logger = LoggerFactory.getLogger(QualityQuestions.class.getCanonicalName());

    @Autowired
    private QualityQuestions qualityQuestions;

    @GetMapping
    public ResponseEntity<QualityQuestions> getQuestions() {
        return new ResponseEntity<>(qualityQuestions, HttpStatus.OK);
    }
}
