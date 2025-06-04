package com.panotify.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an exam in the PaNotify system.
 * An exam contains questions, is associated with a course, and has properties
 * such as deadline, duration, and total score.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class Exam {
    /** Unique identifier for the exam */
    private int examId;
    
    /** ID of the course this exam belongs to */
    private int courseId;
    
    /** ID of the instructor who created this exam */
    private int instructorId;
    
    /** Title of the exam */
    private String title;
    
    /** Maximum possible score for the exam */
    private int totalScore;
    
    /** Timestamp when the exam was created */
    private LocalDateTime createdAt;
    
    /** List of questions in this exam */
    private List<Question> questions;
    
    /** Deadline for taking the exam */
    private LocalDateTime deadline;
    
    /** Duration of the exam in minutes */
    private int durationMinutes;
    
    /** Whether the exam has been published to students */
    private boolean published;

    /**
     * Default constructor for Exam.
     * Initializes an empty list for questions.
     */
    public Exam() {
        this.questions = new ArrayList<>();
    }

    /**
     * Parameterized constructor to create a new exam with essential information.
     * 
     * @param title Title of the exam
     * @param courseId ID of the course this exam belongs to
     * @param instructorId ID of the instructor who created this exam
     */
    public Exam(String title, int courseId, int instructorId) {
        this.title = title;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.createdAt = LocalDateTime.now();
        this.questions = new ArrayList<>();
        this.published = false;
    }

    /**
     * Gets the exam ID.
     * 
     * @return The exam ID
     */
    public int getExamId() {
        return examId;
    }

    /**
     * Sets the exam ID.
     * 
     * @param examId The exam ID to set
     */
    public void setExamId(int examId) {
        this.examId = examId;
    }

    /**
     * Gets the course ID.
     * 
     * @return The course ID
     */
    public int getCourseId() {
        return courseId;
    }

    /**
     * Sets the course ID.
     * 
     * @param courseId The course ID to set
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    /**
     * Gets the instructor ID.
     * 
     * @return The instructor ID
     */
    public int getInstructorId() {
        return instructorId;
    }

    /**
     * Sets the instructor ID.
     * 
     * @param instructorId The instructor ID to set
     */
    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    /**
     * Gets the title of the exam.
     * 
     * @return The exam title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the exam.
     * 
     * @param title The exam title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the exam title (alias for getTitle).
     * 
     * @return The exam title
     * @deprecated Use {@link #getTitle()} instead
     */
    @Deprecated
    public String getExamTitle() {
        return title;
    }

    /**
     * Sets the exam title (alias for setTitle).
     * 
     * @param examTitle The exam title to set
     * @deprecated Use {@link #setTitle(String)} instead
     */
    @Deprecated
    public void setExamTitle(String examTitle) {
        this.title = examTitle;
    }

    /**
     * Gets the total possible score for the exam.
     * 
     * @return The total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Sets the total possible score for the exam.
     * 
     * @param totalScore The total score to set
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Gets the timestamp when the exam was created.
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when the exam was created.
     * 
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the list of questions in this exam.
     * 
     * @return List of questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Sets the list of questions in this exam.
     * 
     * @param questions List of questions to set
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * Adds a question to the exam if it's not already present.
     * 
     * @param question The question to add
     */
    public void addQuestion(Question question) {
        if (!questions.contains(question)) {
            questions.add(question);
        }
    }

    /**
     * Gets the deadline for taking the exam.
     * 
     * @return The deadline
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }

    /**
     * Sets the deadline for taking the exam.
     * 
     * @param deadline The deadline to set
     */
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    /**
     * Gets the due date of the exam (alias for getDeadline).
     * 
     * @return The due date
     * @deprecated Use {@link #getDeadline()} instead
     */
    @Deprecated
    public LocalDateTime getDueDate() {
        return deadline;
    }

    /**
     * Gets the total marks for the exam (alias for getTotalScore).
     * 
     * @return The total marks
     * @deprecated Use {@link #getTotalScore()} instead
     */
    @Deprecated
    public int getTotalMarks() {
        return totalScore;
    }

    /**
     * Gets the duration of the exam in minutes.
     * 
     * @return The duration in minutes
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Sets the duration of the exam in minutes.
     * 
     * @param durationMinutes The duration in minutes to set
     */
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    /**
     * Checks if the exam has been published to students.
     * 
     * @return true if published, false otherwise
     */
    public boolean isPublished() {
        return published;
    }

    /**
     * Sets whether the exam has been published to students.
     * 
     * @param published The published status to set
     */
    public void setPublished(boolean published) {
        this.published = published;
    }

    /**
     * Returns a string representation of the Exam object.
     * 
     * @return A string containing the exam's key information
     */
    @Override
    public String toString() {
        return "Exam{" +
                "examId=" + examId +
                ", title='" + title + '\'' +
                ", courseId=" + courseId +
                ", instructorId=" + instructorId +
                ", deadline=" + deadline +
                ", durationMinutes=" + durationMinutes +
                ", published=" + published +
                ", createdAt=" + createdAt +
                '}';
    }
} 