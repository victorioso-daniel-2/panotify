package com.panotify.model;

import java.time.LocalDateTime;

/**
 * Represents a student's enrollment in a course within the PaNotify system.
 * Contains information about the enrollment relationship between a student and a course.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class CourseEnrollment {
    /** ID of the student enrolled in the course */
    private int userId;
    
    /** ID of the course the student is enrolled in */
    private int courseId;
    
    /** Timestamp when the enrollment occurred */
    private LocalDateTime enrollmentDate;
    
    /** Whether the enrollment is currently active */
    private boolean isActive;

    /** Name of the course (for display purposes) */
    private String courseName;
    
    /** Name of the student (for display purposes) */
    private String studentName;
    
    /** Email of the student (for contact purposes) */
    private String studentEmail;

    /**
     * Default constructor for CourseEnrollment.
     * Initializes the enrollment date to the current time and sets active status to true.
     */
    public CourseEnrollment() {
        this.enrollmentDate = LocalDateTime.now();
        this.isActive = true;
    }

    /**
     * Parameterized constructor to create a new course enrollment with essential information.
     * 
     * @param userId ID of the student
     * @param courseId ID of the course
     */
    public CourseEnrollment(int userId, int courseId) {
        this.userId = userId;
        this.courseId = courseId;
        this.enrollmentDate = LocalDateTime.now();
        this.isActive = true;
    }

    /**
     * Gets the user (student) ID.
     * 
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user (student) ID.
     * 
     * @param userId The user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
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
     * Sets whether the enrollment is active.
     * 
     * @param isActive The active status to set
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Gets the course name.
     * 
     * @return The course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the course name.
     * 
     * @param courseName The course name to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Gets the student name.
     * 
     * @return The student name
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Sets the student name.
     * 
     * @param studentName The student name to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * Gets the student email.
     * 
     * @return The student email
     */
    public String getStudentEmail() {
        return studentEmail;
    }

    /**
     * Sets the student email.
     * 
     * @param studentEmail The student email to set
     */
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    /**
     * Returns a string representation of the CourseEnrollment object.
     * 
     * @return A string containing the enrollment's key information
     */
    @Override
    public String toString() {
        return "CourseEnrollment{" +
                "userId=" + userId +
                ", courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                ", isActive=" + isActive +
                '}';
    }
} 