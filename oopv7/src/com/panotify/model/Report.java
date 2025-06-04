package com.panotify.model;

import java.time.LocalDateTime;

public class Report {
    private int reportId;
    private int studentId;
    private int examId;
    private int totalScore;
    private int maxScore;
    private float averageScore;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private String status;  // "in_progress", "completed", "timeout", "submitted"
    
    // Display fields
    private String examTitle;
    private String studentName;
    private String courseName;
    
    // Constructors
    public Report() {
        this.startedAt = LocalDateTime.now();
        this.status = "in_progress";
    }
    
    public Report(int studentId, int examId, int totalScore, int maxScore) {
        this.studentId = studentId;
        this.examId = examId;
        this.totalScore = totalScore;
        this.maxScore = maxScore;
        this.averageScore = (float) totalScore / maxScore * 100;
        this.startedAt = LocalDateTime.now();
        this.submittedAt = LocalDateTime.now();
        this.status = "completed";
    }
    
    // Getters and Setters
    public int getReportId() {
        return reportId;
    }
    
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getExamId() {
        return examId;
    }
    
    public void setExamId(int examId) {
        this.examId = examId;
    }
    
    public int getTotalScore() {
        return totalScore;
    }
    
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
        updateAverageScore();
    }
    
    public int getMaxScore() {
        return maxScore;
    }
    
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
        updateAverageScore();
    }
    
    public float getAverageScore() {
        return averageScore;
    }
    
    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }
    
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
    
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public String getExamTitle() {
        return examTitle;
    }
    
    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    private void updateAverageScore() {
        if (maxScore > 0) {
            this.averageScore = (float) totalScore / maxScore * 100;
        }
        this.submittedAt = LocalDateTime.now();
        this.status = "completed";
    }
} 