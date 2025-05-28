package com.panotify.model;

import java.time.LocalDateTime;

/**
 * Represents a student's enrollment in a course.
 * Contains enrollment details, status, and display information.
 */
public class CourseEnrollment {
    private int userId;
    private int courseId;
    private LocalDateTime enrollmentDate;
    private boolean isActive;
    
    // For display purposes
    private String courseName;
    private String studentName;
    private String studentEmail;
    
    /**
     * Default constructor for CourseEnrollment class.
     * Sets enrollment date to now and active status to true.
     */
    public CourseEnrollment() {
        this.enrollmentDate = LocalDateTime.now();
        this.isActive = true;
    }
    
    /**
     * Constructs a new CourseEnrollment with the specified user and course IDs.
     * Sets enrollment date to now and active status to true.
     * 
     * @param userId The ID of the user
     * @param courseId The ID of the course
     */
    public CourseEnrollment(int userId, int courseId) {
        this.userId = userId;
        this.courseId = courseId;
        this.enrollmentDate = LocalDateTime.now();
        this.isActive = true;
    }
    
    /**
     * Gets the user ID for this enrollment.
     * 
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * Sets the user ID for this enrollment.
     * 
     * @param userId The user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    /**
     * Gets the course ID for this enrollment.
     * 
     * @return The course ID
     */
    public int getCourseId() {
        return courseId;
    }
    
    /**
     * Sets the course ID for this enrollment.
     * 
     * @param courseId The course ID to set
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    /**
     * Gets the enrollment date.
     * 
     * @return The enrollment date
     */
    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }
    
    /**
     * Sets the enrollment date.
     * 
     * @param enrollmentDate The enrollment date to set
     */
    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    /**
     * Checks if the enrollment is active.
     * 
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Sets the active status of the enrollment.
     * 
     * @param isActive true if active, false otherwise
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    /**
     * Gets the course name for display purposes.
     * 
     * @return The course name
     */
    public String getCourseName() {
        return courseName;
    }
    
    /**
     * Sets the course name for display purposes.
     * 
     * @param courseName The course name to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    /**
     * Gets the student name for display purposes.
     * 
     * @return The student name
     */
    public String getStudentName() {
        return studentName;
    }
    
    /**
     * Sets the student name for display purposes.
     * 
     * @param studentName The student name to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    /**
     * Gets the student email for display purposes.
     * 
     * @return The student email
     */
    public String getStudentEmail() {
        return studentEmail;
    }
    
    /**
     * Sets the student email for display purposes.
     * 
     * @param studentEmail The student email to set
     */
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
    
    @Override
    public String toString() {
        return "CourseEnrollment{" +
                "userId=" + userId +
                ", courseId=" + courseId +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
} 