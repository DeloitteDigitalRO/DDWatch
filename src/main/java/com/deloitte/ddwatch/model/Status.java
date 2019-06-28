package com.deloitte.ddwatch.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Optional;

public enum Status {
    RED(1, "R"),
    AMBER(2, "A"),
    GREEN(3, "G"),
    UNDEFINED(0, "");

    private final int priority;
    private final String excelCode;

    Status(int priority, String excelCode) {
        this.priority = priority;
        this.excelCode = excelCode;
    }

    public int getPriority() {
        return priority;
    }

    public String getExcelCode() {
        return excelCode;
    };

    public static Status getStatusByOverallCoverage(double coverage) {
        if (coverage < 0 || coverage > 100) {
            throw new RuntimeException("invalid coverage value");
        }
        if (coverage < 50) {
            return RED;
        } else if (coverage < 60) {
            return AMBER;
        } else {
            return GREEN;
        }
    }

    public static Status getStatusByDefectDensity(double defectDensity) {
        if (defectDensity < 0 || defectDensity > 100) {
            throw new RuntimeException("invalid defect Density");
        }
        if (defectDensity < 2) {
            return GREEN;
        } else if (defectDensity < 5) {
            return AMBER;
        } else {
            return RED;
        }
    }

    public static Status getStatusByExcelCode(String code) {
        return Arrays.stream(values())
                .filter(status -> status.getExcelCode().equals(code))
                .findFirst()
                .orElse(UNDEFINED);
    }

    public static String getFormatedString(Status status){
        switch (status){
            case AMBER:
                return "MEDIUM RISK";
            case RED:
                return "HIGH RISK";
            case GREEN:
                return "LOW RISK";
            default:
                return "";

        }
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
