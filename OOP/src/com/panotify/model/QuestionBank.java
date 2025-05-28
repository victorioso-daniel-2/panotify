package com.panotify.model;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionBank {
    private int bankId;
    private String name;
    private int instructorId;
    private LocalDateTime createdAt;
    private List<Question> questions;
    
    public QuestionBank() {
        this.createdAt = LocalDateTime.now();
    }
    
    public QuestionBank(String name, int instructorId) {
        this.name = name;
        this.instructorId = instructorId;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getBankId() { return bankId; }
    public void setBankId(int bankId) { this.bankId = bankId; }
    
    public String getName() { return name; }  
    public void setName(String name) { this.name = name; }
    
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    
    @Override
    public String toString() {
        return "QuestionBank{" +
                "bankId=" + bankId +
                ", name='" + name + '\'' +
                ", instructorId=" + instructorId +
                ", createdAt=" + createdAt +
                '}';
    }
}