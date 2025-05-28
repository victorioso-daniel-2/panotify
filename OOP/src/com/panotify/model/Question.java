package com.panotify.model;

/**
 * Represents a question in an exam.
 * Contains the question text, correct answer, and point value.
 * Each question belongs to a specific exam.
 */
public class Question {
    private int questionId;
    private int examId;
    private String questionText;
    private String correctAnswer;
    private int points;
    
    /**
     * Default constructor for Question class.
     */
    public Question() {}
    
    /**
     * Constructs a new Question with the specified details.
     * 
     * @param examId The ID of the exam this question belongs to
     * @param questionText The text of the question
     * @param correctAnswer The correct answer to the question
     * @param points The number of points this question is worth
     */
    public Question(int examId, String questionText, String correctAnswer, int points) {
        this.examId = examId;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.points = points;
    }
    
    /**
     * Gets the unique identifier for this question.
     * 
     * @return The question ID
     */
    public int getQuestionId() {
        return questionId;
    }
    
    /**
     * Sets the unique identifier for this question.
     * 
     * @param questionId The question ID to set
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    
    /**
     * Gets the ID of the exam this question belongs to.
     * 
     * @return The exam ID
     */
    public int getExamId() {
        return examId;
    }
    
    /**
     * Sets the ID of the exam this question belongs to.
     * 
     * @param examId The exam ID to set
     */
    public void setExamId(int examId) {
        this.examId = examId;
    }
    
    /**
     * Gets the text of this question.
     * 
     * @return The question text
     */
    public String getQuestionText() {
        return questionText;
    }
    
    /**
     * Sets the text of this question.
     * 
     * @param questionText The question text to set
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    /**
     * Gets the correct answer for this question.
     * 
     * @return The correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    /**
     * Sets the correct answer for this question.
     * 
     * @param correctAnswer The correct answer to set
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    /**
     * Gets the number of points this question is worth.
     * 
     * @return The point value
     */
    public int getPoints() {
        return points;
    }
    
    /**
     * Sets the number of points this question is worth.
     * 
     * @param points The point value to set
     */
    public void setPoints(int points) {
        this.points = points;
    }
} 