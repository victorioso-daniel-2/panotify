package com.panotify.model;

import java.time.LocalDateTime;

/**
 * Represents a student's answer to a specific question in an exam.
 * Contains answer text, correctness, and submission time.
 */
public class StudentAnswer {
    private int answerId;
    private int studentId;
    private int questionId;
    private String answerText;
    private boolean isCorrect;
    private LocalDateTime submittedAt;

    /**
     * Default constructor for StudentAnswer class.
     * Sets submittedAt to now.
     */
    public StudentAnswer() {
        this.submittedAt = LocalDateTime.now();
    }

    /**
     * Constructs a new StudentAnswer with the specified details.
     * Sets submittedAt to now.
     *
     * @param studentId The ID of the student
     * @param questionId The ID of the question
     * @param answerText The answer text
     */
    public StudentAnswer(int studentId, int questionId, String answerText) {
        this.studentId = studentId;
        this.questionId = questionId;
        this.answerText = answerText;
        this.submittedAt = LocalDateTime.now();
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
     * Gets the student ID for this answer.
     *
     * @return The student ID
     */
    public int getStudentId() {
        return studentId;
    }

    /**
     * Sets the student ID for this answer.
     *
     * @param studentId The student ID to set
     */
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    /**
     * Gets the question ID for this answer.
     *
     * @return The question ID
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * Sets the question ID for this answer.
     *
     * @param questionId The question ID to set
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    /**
     * Gets the answer text.
     *
     * @return The answer text
     */
    public String getAnswerText() {
        return answerText;
    }

    /**
     * Sets the answer text.
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
     * @param correct true if correct, false otherwise
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
}