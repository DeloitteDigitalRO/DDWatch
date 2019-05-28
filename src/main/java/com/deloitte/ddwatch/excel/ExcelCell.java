package com.deloitte.ddwatch.excel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class ExcelCell {
    private int row, col;
    private String type;
}
