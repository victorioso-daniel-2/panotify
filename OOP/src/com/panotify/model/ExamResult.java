package com.panotify.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the result of a student's attempt at an exam.
 * Contains score, status, timing, and answer details for the exam attempt.
 */
public class ExamResult {
    private int resultId;
    private int examId;
    private int studentId;
    private String examTitle;
    private String studentName;
    private int totalScore;
    private int maxScore;
    private double percentage;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private boolean isCompleted;
    private String status; // "in_progress", "completed", "timeout", "submitted"
    private List<StudentAnswer> studentAnswers;
    
    /**
     * Default constructor for ExamResult class.
     */
    public ExamResult() {}
    
    /**
     * Constructs a new ExamResult for a specific exam and student.
     * 
     * @param examId The ID of the exam
     * @param studentId The ID of the student
     */
    public ExamResult(int examId, int studentId) {
        this.examId = examId;
        this.studentId = studentId;
        this.isCompleted = false;
        this.status = "in_progress";
    }
    
    /**
     * Gets the unique identifier for this exam result.
     * 
     * @return The result ID
     */
    public int getResultId() {
        return resultId;
    }
    
    /**
     * Sets the unique identifier for this exam result.
     * 
     * @param resultId The result ID to set
     */
    public void setResultId(int resultId) {
        this.resultId = resultId;
    }
    
    /**
     * Gets the exam ID for this result.
     * 
     * @return The exam ID
     */
    public int getExamId() {
        return examId;
    }
    
    /**
     * Sets the exam ID for this result.
     * 
     * @param examId The exam ID to set
     */
    public void setExamId(int examId) {
        this.examId = examId;
    }
    
    /**
     * Gets the student ID for this result.
     * 
     * @return The student ID
     */
    public int getStudentId() {
        return studentId;
    }
    
    /**
     * Sets the student ID for this result.
     * 
     * @param studentId The student ID to set
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    /**
     * Gets the title of the exam.
     * 
     * @return The exam title
     */
    public String getExamTitle() {
        return examTitle;
    }
    
    /**
     * Sets the title of the exam.
     * 
     * @param examTitle The exam title to set
     */
    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }
    
    /**
     * Gets the name of the student.
     * 
     * @return The student name
     */
    public String getStudentName() {
        return studentName;
    }
    
    /**
     * Sets the name of the student.
     * 
     * @param studentName The student name to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
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
     * Sets the total score achieved in the exam.
     * 
     * @param totalScore The total score to set
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
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
     * Sets the maximum possible score for the exam.
     * 
     * @param maxScore The maximum score to set
     */
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
    
    /**
     * Gets the percentage score for the exam.
     * 
     * @return The percentage score
     */
    public double getPercentage() {
        return percentage;
    }
    
    /**
     * Sets the percentage score for the exam.
     * 
     * @param percentage The percentage score to set
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
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
     * Checks if the exam attempt is completed.
     * 
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return isCompleted;
    }
    
    /**
     * Sets the completion status of the exam attempt.
     * 
     * @param completed true if completed, false otherwise
     */
    public void setCompleted(boolean completed) {
        isCompleted = completed;
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
     * Gets the list of student answers for this exam attempt.
     * 
     * @return List of StudentAnswer objects
     */
    public List<StudentAnswer> getStudentAnswers() {
        return studentAnswers;
    }
    
    /**
     * Sets the list of student answers for this exam attempt.
     * 
     * @param studentAnswers List of StudentAnswer objects to set
     */
    public void setStudentAnswers(List<StudentAnswer> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }
    
    /**
     * Returns a string representation of the ExamResult object.
     * 
     * @return A string containing exam and student details
     */
    @Override
    public String toString() {
        return "ExamResult{" +
                "resultId=" + resultId +
                ", examTitle='" + examTitle + '\'' +
                ", studentName='" + studentName + '\'' +
                ", totalScore=" + totalScore +
                ", maxScore=" + maxScore +
                ", percentage=" + percentage +
                '}';
    }
}
