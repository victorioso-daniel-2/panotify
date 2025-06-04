package com.panotify.model;

public class QuestionResult {
    private int questionResultId;
    private int resultId;
    private int questionId;
    private int questionNumber;
    private String questionText;
    private String studentAnswer;
    private String correctAnswer;
    private boolean correct;
    
    public QuestionResult(int questionResultId, int resultId, int questionId, int questionNumber, 
                        String questionText, String studentAnswer, String correctAnswer, boolean correct) {
        this.questionResultId = questionResultId;
        this.resultId = resultId;
        this.questionId = questionId;
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.studentAnswer = studentAnswer;
        this.correctAnswer = correctAnswer;
        this.correct = correct;
    }
    
    // Getters and setters
    public int getQuestionResultId() {
        return questionResultId;
    }
    
    public void setQuestionResultId(int questionResultId) {
        this.questionResultId = questionResultId;
    }
    
    public int getResultId() {
        return resultId;
    }
    
    public void setResultId(int resultId) {
        this.resultId = resultId;
    }
    
    public int getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    
    public int getQuestionNumber() {
        return questionNumber;
    }
    
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    public String getStudentAnswer() {
        return studentAnswer;
    }
    
    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }
    
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    public boolean isCorrect() {
        return correct;
    }
    
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
} 