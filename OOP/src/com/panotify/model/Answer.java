package com.panotify.model;

import java.time.LocalDateTime;

public class Answer {
    private int answerId;
    private int attemptId;
    private int questionId;
    private String studentAnswer;
    private boolean isCorrect;
    private LocalDateTime timestamp;
    
    public Answer() {
        this.timestamp = LocalDateTime.now();
    }
    
    public Answer(int attemptId, int questionId, String studentAnswer, boolean isCorrect) {
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.studentAnswer = studentAnswer;
        this.isCorrect = isCorrect;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getAnswerId() { return answerId; }
    public void setAnswerId(int answerId) { this.answerId = answerId; }
    
    public int getAttemptId() { return attemptId; }
    public void setAttemptId(int attemptId) { this.attemptId = attemptId; }
    
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    
    public String getStudentAnswer() { return studentAnswer; }
    public void setStudentAnswer(String studentAnswer) { this.studentAnswer = studentAnswer; }
    
    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    @Override
    public String toString() {
        return "Answer{" +
                "answerId=" + answerId +
                ", attemptId=" + attemptId +
                ", questionId=" + questionId +
                ", studentAnswer='" + studentAnswer + '\'' +
                ", isCorrect=" + isCorrect +
                ", timestamp=" + timestamp +
                '}';
    }
}