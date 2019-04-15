package com.deloitte.ddwatch.model;

public enum Status {
    RED(1), GREEN(3), AMBER(2);

    private final int priority;


    Status(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

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

    @Override
    public String toString() {
        return "Status{" +
                "priority=" + priority +
                '}';
    }
}
