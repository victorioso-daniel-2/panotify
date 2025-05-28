package com.panotify.model;

import java.time.LocalDateTime;

/**
 * Represents a summary report of a student's exam attempt.
 * Contains scores, status, timing, and display information for reporting.
 */
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
    
    /**
     * Default constructor for Report class.
     * Sets startedAt to now and status to "in_progress".
     */
    public Report() {
        this.startedAt = LocalDateTime.now();
        this.status = "in_progress";
    }
    
    /**
     * Constructs a new Report with the specified details.
     * Sets startedAt and submittedAt to now and status to "completed".
     * 
     * @param studentId The ID of the student
     * @param examId The ID of the exam
     * @param totalScore The total score achieved
     * @param maxScore The maximum possible score
     */
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
    
    /**
     * Gets the unique identifier for this report.
     * 
     * @return The report ID
     */
    public int getReportId() {
        return reportId;
    }
    
    /**
     * Sets the unique identifier for this report.
     * 
     * @param reportId The report ID to set
     */
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
    
    /**
     * Gets the student ID for this report.
     * 
     * @return The student ID
     */
    public int getStudentId() {
        return studentId;
    }
    
    /**
     * Sets the student ID for this report.
     * 
     * @param studentId The student ID to set
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    /**
     * Gets the exam ID for this report.
     * 
     * @return The exam ID
     */
    public int getExamId() {
        return examId;
    }
    
    /**
     * Sets the exam ID for this report.
     * 
     * @param examId The exam ID to set
     */
    public void setExamId(int examId) {
        this.examId = examId;
    }
    
    /**
     * Gets the total score achieved in the exam.
     * 
     * @return The total score
     */
    public int getTotalScore() {
        return totalScore;
    }
    
    /**
     * Sets the total score achieved in the exam and updates the average score.
     * 
     * @param totalScore The total score to set
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
        updateAverageScore();
    }
    
    /**
     * Gets the maximum possible score for the exam.
     * 
     * @return The maximum score
     */
    public int getMaxScore() {
        return maxScore;
    }
    
    /**
     * Sets the maximum possible score for the exam and updates the average score.
     * 
     * @param maxScore The maximum score to set
     */
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
        updateAverageScore();
    }
    
    /**
     * Gets the average score as a percentage.
     * 
     * @return The average score percentage
     */
    public float getAverageScore() {
        return averageScore;
    }
    
    /**
     * Sets the average score as a percentage.
     * 
     * @param averageScore The average score to set
     */
    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }
    
    /**
     * Gets the timestamp when the exam was started.
     * 
     * @return The start timestamp
     */
    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    /**
     * Sets the timestamp when the exam was started.
     * 
     * @param startedAt The start timestamp to set
     */
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
    
    /**
     * Gets the timestamp when the exam was submitted.
     * 
     * @return The submission timestamp
     */
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    
    /**
     * Sets the timestamp when the exam was submitted.
     * 
     * @param submittedAt The submission timestamp to set
     */
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    /**
     * Gets the title of the exam for display purposes.
     * 
     * @return The exam title
     */
    public String getExamTitle() {
        return examTitle;
    }
    
    /**
     * Sets the title of the exam for display purposes.
     * 
     * @param examTitle The exam title to set
     */
    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }
    
    /**
     * Gets the student name for display purposes.
     * 
     * @return The student name
     */
    public String getStudentName() {
        return studentName;
    }
    
    /**
     * Sets the student name for display purposes.
     * 
     * @param studentName The student name to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    /**
     * Gets the course name for display purposes.
     * 
     * @return The course name
     */
    public String getCourseName() {
        return courseName;
    }
    
    /**
     * Sets the course name for display purposes.
     * 
     * @param courseName The course name to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    /**
     * Gets the status of the exam attempt.
     * 
     * @return The status string
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Sets the status of the exam attempt.
     * 
     * @param status The status string to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Updates the average score and sets submittedAt and status to completed.
     */
    private void updateAverageScore() {
        if (maxScore > 0) {
            this.averageScore = (float) totalScore / maxScore * 100;
        }
        this.submittedAt = LocalDateTime.now();
        this.status = "completed";
    }
} 