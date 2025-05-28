package com.panotify.model;

import java.time.LocalDateTime;

public class Question {
    private int questionId;
    private String text;
    private String options; // JSON string containing options
    private String correctAnswer;
    private int instructorId;
    private LocalDateTime createdAt;
    
    public Question() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Question(String text, String options, String correctAnswer, int instructorId) {
        this.text = text;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.instructorId = instructorId;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }
    
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", text='" + text + '\'' +
                ", options='" + options + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", instructorId=" + instructorId +
                ", createdAt=" + createdAt +
                '}';
    }
}