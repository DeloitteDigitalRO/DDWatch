package com.deloitte.ddwatch.excel;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class CellPosition {
    private int row, col;
}
