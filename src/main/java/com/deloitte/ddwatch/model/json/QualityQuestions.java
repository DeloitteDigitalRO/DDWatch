package com.deloitte.ddwatch.model.json;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QualityQuestions {

    List<Question> questions;
}
