package com.panotify.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents the result of a student's exam attempt in the PaNotify system.
 * Contains information about the exam, student, scores, and submission status.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class ExamResult {
    /** Unique identifier for the exam result */
    private int resultId;
    
    /** ID of the exam this result belongs to */
    private int examId;
    
    /** ID of the student who took the exam */
    private int studentId;
    
    /** Title of the exam */
    private String examTitle;
    
    /** Full name of the student */
    private String studentName;
    
    /** Points scored by the student */
    private int totalScore;
    
    /** Maximum possible points for the exam */
    private int maxScore;
    
    /** Percentage score (totalScore / maxScore * 100) */
    private double percentage;
    
    /** Timestamp when the student started the exam */
    private LocalDateTime startedAt;
    
    /** Timestamp when the student submitted the exam */
    private LocalDateTime submittedAt;
    
    /** Whether the exam has been completed */
    private boolean isCompleted;
    
    /** Current status of the exam attempt ("in_progress", "completed", "timeout", "submitted") */
    private String status;
    
    /** List of student's answers to exam questions */
    private List<StudentAnswer> studentAnswers;

    /**
     * Default constructor for ExamResult.
     */
    public ExamResult() {}

    /**
     * Parameterized constructor to create a new exam result with essential information.
     * 
     * @param examId ID of the exam
     * @param studentId ID of the student
     */
    public ExamResult(int examId, int studentId) {
        this.examId = examId;
        this.studentId = studentId;
        this.isCompleted = false;
        this.status = "in_progress";
    }

    /**
     * Gets the result ID.
     * 
     * @return The result ID
     */
    public int getResultId() {
        return resultId;
    }

    /**
     * Sets the result ID.
     * 
     * @param resultId The result ID to set
     */
    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    /**
     * Gets the exam ID.
     * 
     * @return The exam ID
     */
    public int getExamId() {
        return examId;
    }

    /**
     * Sets the exam ID.
     * 
     * @param examId The exam ID to set
     */
    public void setExamId(int examId) {
        this.examId = examId;
    }

    /**
     * Gets the student ID.
     * 
     * @return The student ID
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * Sets the student ID.
     * 
     * @param studentId The student ID to set
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /**
     * Gets the exam title.
     * 
     * @return The exam title
     */
    public String getExamTitle() {
        return examTitle;
    }

    /**
     * Sets the exam title.
     * 
     * @param examTitle The exam title to set
     */
    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    /**
     * Gets the student's name.
     * 
     * @return The student's name
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Sets the student's name.
     * 
     * @param studentName The student's name to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * Gets the total score achieved by the student.
     * 
     * @return The total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Sets the total score achieved by the student.
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
     * Gets the percentage score (totalScore / maxScore * 100).
     * 
     * @return The percentage score
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * Sets the percentage score.
     * 
     * @param percentage The percentage score to set
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Gets the timestamp when the student started the exam.
     * 
     * @return The start timestamp
     */
    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    /**
     * Sets the timestamp when the student started the exam.
     * 
     * @param startedAt The start timestamp to set
     */
    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    /**
     * Gets the timestamp when the student submitted the exam.
     * 
     * @return The submission timestamp
     */
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    /**
     * Sets the timestamp when the student submitted the exam.
     * 
     * @param submittedAt The submission timestamp to set
     */
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    /**
     * Gets the submission date (alias for getSubmittedAt).
     * 
     * @return The submission date
     * @deprecated Use {@link #getSubmittedAt()} instead
     */
    @Deprecated
    public LocalDateTime getSubmissionDate() {
        return submittedAt;
    }

    /**
     * Checks if the exam has been completed.
     * 
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Sets whether the exam has been completed.
     * 
     * @param completed The completed status to set
     */
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    /**
     * Gets the current status of the exam attempt.
     * 
     * @return The status ("in_progress", "completed", "timeout", or "submitted")
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the exam attempt.
     * 
     * @param status The status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the list of student's answers to exam questions.
     * 
     * @return List of student answers
     */
    public List<StudentAnswer> getStudentAnswers() {
        return studentAnswers;
    }

    /**
     * Sets the list of student's answers to exam questions.
     * 
     * @param studentAnswers List of student answers to set
     */
    public void setStudentAnswers(List<StudentAnswer> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }

    /**
     * Returns a string representation of the ExamResult object.
     * 
     * @return A string containing the exam result's key information
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