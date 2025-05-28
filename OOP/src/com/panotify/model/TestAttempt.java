package com.panotify.model;

import java.time.LocalDateTime;

public class TestAttempt {
    private int attemptId;
    private int studentId;
    private int testId;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    
    public TestAttempt() {
        this.startedAt = LocalDateTime.now();
    }
    
    public TestAttempt(int studentId, int testId) {
        this.studentId = studentId;
        this.testId = testId;
        this.startedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getAttemptId() { return attemptId; }
    public void setAttemptId(int attemptId) { this.attemptId = attemptId; }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public int getTestId() { return testId; }
    public void setTestId(int testId) { this.testId = testId; }
    
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public boolean isCompleted() {
        return completedAt != null;
    }
    
    @Override
    public String toString() {
        return "TestAttempt{" +
                "attemptId=" + attemptId +
                ", studentId=" + studentId +
                ", testId=" + testId +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                '}';
    }
}