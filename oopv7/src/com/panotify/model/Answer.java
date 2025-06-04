package com.panotify.model;

public class Answer {
    private int answerId;
    private int questionId;
    private String answerText;
    private boolean isCorrect;
    private int orderNumber;
    
    // Constructors
    public Answer() {}
    
    public Answer(int questionId, String answerText, boolean isCorrect) {
        this.questionId = questionId;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }
    
    // Getters and Setters
    public int getAnswerId() {
        return answerId;
    }
    
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
    
    public int getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    
    public String getAnswerText() {
        return answerText;
    }
    
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
    
    public boolean isCorrect() {
        return isCorrect;
    }
    
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
    
    public int getOrderNumber() {
        return orderNumber;
    }
    
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    @Override
    public String toString() {
        return answerText;
    }
} 