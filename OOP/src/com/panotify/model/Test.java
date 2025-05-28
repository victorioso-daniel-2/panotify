package com.panotify.model;

import java.time.LocalDateTime;
import java.util.List;

public class Test {
    private int testId;
    private String title;
    private String description;
    private int durationMinutes;
    private int instructorId;
    private LocalDateTime createdAt;
    private boolean isActive;
    private List<Question> questions;
    
    public Test() {
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
    
    public Test(String title, String description, int durationMinutes, int instructorId) {
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.instructorId = instructorId;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }
    
    // Getters and Setters
    public int getTestId() { return testId; }
    public void setTestId(int testId) { this.testId = testId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    
    @Override
    public String toString() {
        return "Test{" +
                "testId=" + testId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", instructorId=" + instructorId +
                ", createdAt=" + createdAt +
                ", isActive=" + isActive +
                '}';
    }
}