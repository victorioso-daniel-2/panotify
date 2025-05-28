package com.panotify.model;

/**
 * Represents an answer option for a question in an exam.
 * Contains the answer text, correctness, and order number for display.
 */
public class Answer {
    private int answerId;
    private int questionId;
    private String answerText;
    private boolean isCorrect;
    private int orderNumber;
    
    /**
     * Default constructor for Answer class.
     */
    public Answer() {}
    
    /**
     * Constructs a new Answer with the specified details.
     * 
     * @param questionId The ID of the question this answer belongs to
     * @param answerText The text of the answer
     * @param isCorrect Whether this answer is correct
     */
    public Answer(int questionId, String answerText, boolean isCorrect) {
        this.questionId = questionId;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }
    
    /**
     * Gets the unique identifier for this answer.
     * 
     * @return The answer ID
     */
    public int getAnswerId() {
        return answerId;
    }
    
    /**
     * Sets the unique identifier for this answer.
     * 
     * @param answerId The answer ID to set
     */
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
    
    /**
     * Gets the ID of the question this answer belongs to.
     * 
     * @return The question ID
     */
    public int getQuestionId() {
        return questionId;
    }
    
    /**
     * Sets the ID of the question this answer belongs to.
     * 
     * @param questionId The question ID to set
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    
    /**
     * Gets the text of this answer.
     * 
     * @return The answer text
     */
    public String getAnswerText() {
        return answerText;
    }
    
    /**
     * Sets the text of this answer.
     * 
     * @param answerText The answer text to set
     */
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
    
    /**
     * Checks if this answer is correct.
     * 
     * @return true if correct, false otherwise
     */
    public boolean isCorrect() {
        return isCorrect;
    }
    
    /**
     * Sets whether this answer is correct.
     * 
     * @param correct true if correct, false otherwise
     */
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
    
    /**
     * Gets the order number for this answer (for display purposes).
     * 
     * @return The order number
     */
    public int getOrderNumber() {
        return orderNumber;
    }
    
    /**
     * Sets the order number for this answer (for display purposes).
     * 
     * @param orderNumber The order number to set
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    /**
     * Returns the answer text as the string representation.
     * 
     * @return The answer text
     */
    @Override
    public String toString() {
        return answerText;
    }
}
