package com.deloitte.ddwatch.model.json;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
public class Question {

    String id;
    String text;
    Set<Map<String, String>> answers;
}
