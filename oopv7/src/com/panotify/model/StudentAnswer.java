package com.panotify.model;

import java.time.LocalDateTime;

/**
 * Represents a student's answer to a question in an exam.
 * Contains information about the answer, its correctness, and submission time.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class StudentAnswer {
    /** Unique identifier for the answer */
    private int answerId;
    
    /** ID of the student who provided the answer */
    private int studentId;
    
    /** ID of the question being answered */
    private int questionId;
    
    /** The text of the student's answer */
    private String answerText;
    
    /** Whether the answer is correct */
    private boolean isCorrect;
    
    /** Timestamp when the answer was submitted */
    private LocalDateTime submittedAt;

    /**
     * Default constructor for StudentAnswer.
     * Initializes the submission time to the current time.
     */
    public StudentAnswer() {
        this.submittedAt = LocalDateTime.now();
    }

    /**
     * Parameterized constructor to create a new student answer with essential information.
     * 
     * @param studentId ID of the student
     * @param questionId ID of the question
     * @param answerText The text of the student's answer
     */
    public StudentAnswer(int studentId, int questionId, String answerText) {
        this.studentId = studentId;
        this.questionId = questionId;
        this.answerText = answerText;
        this.submittedAt = LocalDateTime.now();
    }

    /**
     * Gets the answer ID.
     * 
     * @return The answer ID
     */
    public int getAnswerId() {
        return answerId;
    }

    /**
     * Sets the answer ID.
     * 
     * @param answerId The answer ID to set
     */
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
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
     * Gets the question ID.
     * 
     * @return The question ID
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * Sets the question ID.
     * 
     * @param questionId The question ID to set
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    /**
     * Gets the text of the student's answer.
     * 
     * @return The answer text
     */
    public String getAnswerText() {
        return answerText;
    }

    /**
     * Sets the text of the student's answer.
     * 
     * @param answerText The answer text to set
     */
    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    /**
     * Checks if the answer is correct.
     * 
     * @return true if correct, false otherwise
     */
    public boolean isCorrect() {
        return isCorrect;
    }

    /**
     * Sets whether the answer is correct.
     * 
     * @param correct The correctness status to set
     */
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    /**
     * Gets the timestamp when the answer was submitted.
     * 
     * @return The submission timestamp
     */
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    /**
     * Sets the timestamp when the answer was submitted.
     * 
     * @param submittedAt The submission timestamp to set
     */
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    /**
     * Returns a string representation of the StudentAnswer object.
     * 
     * @return A string containing the student answer's key information
     */
    @Override
    public String toString() {
        return "StudentAnswer{" +
                "answerId=" + answerId +
                ", studentId=" + studentId +
                ", questionId=" + questionId +
                ", answerText='" + answerText + '\'' +
                ", isCorrect=" + isCorrect +
                ", submittedAt=" + submittedAt +
                '}';
    }
} 