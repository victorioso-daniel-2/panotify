package com.panotify.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Report {
    private int reportId;
    private int studentId;
    private int testId;
    private BigDecimal score;
    private LocalDateTime generatedAt;
    
    public Report() {
        this.generatedAt = LocalDateTime.now();
    }
    
    public Report(int studentId, int testId, BigDecimal score) {
        this.studentId = studentId;
        this.testId = testId;
        this.score = score;
        this.generatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public int getTestId() { return testId; }
    public void setTestId(int testId) { this.testId = testId; }
    
    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }
    
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    
    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", studentId=" + studentId +
                ", testId=" + testId +
                ", score=" + score +
                ", generatedAt=" + generatedAt +
                '}';
    }
}