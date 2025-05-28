package com.panotify.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents an exam in the PaNotify system.
 * Contains exam details, questions, scoring information, and timing constraints.
 * Manages the state of an exam including its publication status and deadlines.
 */
public class Exam {
    private int examId;
    private int courseId;
    private int instructorId;
    private String title;  // For database compatibility
    private String examTitle;  // For UI display
    private int totalScore;
    private LocalDateTime createdAt;
    private List<Question> questions;
    private LocalDateTime deadline;  // Added field for deadline
    private int durationMinutes;  // Added field for exam duration
    private boolean published;  // Added field for publish status
    
    /**
     * Default constructor for Exam class.
     * Initializes an empty question list, sets creation time,
     * and defaults to unpublished status.
     */
    public Exam() {
        this.questions = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.published = false;  // Default to unpublished
    }
    
    /**
     * Constructs a new Exam with the specified details.
     * 
     * @param title The title of the exam
     * @param courseId The ID of the course this exam belongs to
     * @param instructorId The ID of the instructor who created this exam
     */
    public Exam(String title, int courseId, int instructorId) {
        this.title = title;
        this.examTitle = title;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.createdAt = LocalDateTime.now();
        this.questions = new ArrayList<>();
        this.published = false;  // Default to unpublished
    }
    
    /**
     * Gets the unique identifier for this exam.
     * 
     * @return The exam ID
     */
    public int getExamId() {
        return examId;
    }
    
    /**
     * Sets the unique identifier for this exam.
     * 
     * @param examId The exam ID to set
     */
    public void setExamId(int examId) {
        this.examId = examId;
    }
    
    /**
     * Gets the ID of the course this exam belongs to.
     * 
     * @return The course ID
     */
    public int getCourseId() {
        return courseId;
    }
    
    /**
     * Sets the ID of the course this exam belongs to.
     * 
     * @param courseId The course ID to set
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    /**
     * Gets the ID of the instructor who created this exam.
     * 
     * @return The instructor ID
     */
    public int getInstructorId() {
        return instructorId;
    }
    
    /**
     * Sets the ID of the instructor who created this exam.
     * 
     * @param instructorId The instructor ID to set
     */
    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }
    
    /**
     * Gets the title of the exam (database version).
     * 
     * @return The exam title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title of the exam.
     * Updates both database and UI versions of the title.
     * 
     * @param title The title to set
     */
    public void setTitle(String title) {
        this.title = title;
        this.examTitle = title;  // Keep them in sync
    }
    
    /**
     * Gets the display title of the exam (UI version).
     * Falls back to database title if UI title is not set.
     * 
     * @return The exam title for display
     */
    public String getExamTitle() {
        return examTitle != null ? examTitle : title;
    }
    
    /**
     * Sets the display title of the exam (UI version).
     * 
     * @param examTitle The display title to set
     */
    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }
    
    /**
     * Gets the total score possible for this exam.
     * 
     * @return The total possible score
     */
    public int getTotalScore() {
        return totalScore;
    }
    
    /**
     * Sets the total score possible for this exam.
     * 
     * @param totalScore The total score to set
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    
    /**
     * Gets the timestamp when this exam was created.
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the timestamp when this exam was created.
     * 
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Gets the list of questions in this exam.
     * 
     * @return List of Question objects
     */
    public List<Question> getQuestions() {
        return questions;
    }
    
    /**
     * Sets the list of questions for this exam.
     * 
     * @param questions List of Question objects to set
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    /**
     * Adds a question to this exam.
     * Automatically updates the total score.
     * 
     * @param question The Question object to add
     */
    public void addQuestion(Question question) {
        this.questions.add(question);
        this.totalScore += question.getPoints();
    }
    
    /**
     * Gets the deadline for this exam.
     * 
     * @return The deadline timestamp
     */
    public LocalDateTime getDeadline() {
        return deadline;
    }
    
    /**
     * Sets the deadline for this exam.
     * 
     * @param deadline The deadline timestamp to set
     */
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }
    
    /**
     * Gets the duration of this exam in minutes.
     * 
     * @return The duration in minutes
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }
    
    /**
     * Sets the duration of this exam in minutes.
     * 
     * @param durationMinutes The duration in minutes to set
     */
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    /**
     * Checks if this exam is published and available to students.
     * 
     * @return true if published, false otherwise
     */
    public boolean isPublished() {
        return published;
    }
    
    /**
     * Sets the publication status of this exam.
     * 
     * @param published The publication status to set
     */
    public void setPublished(boolean published) {
        this.published = published;
    }
}
