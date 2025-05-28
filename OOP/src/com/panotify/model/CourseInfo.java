package com.panotify.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents detailed information about a course, including metadata and enrolled students.
 * Used for displaying course details and managing course-related data.
 */
public class CourseInfo {
    private int courseId;
    private String courseName;
    private String courseCode;
    private int instructorId;
    private String instructorName;
    private String description;
    private LocalDateTime createdAt;
    private boolean isActive;
    private List<Student> enrolledStudents;
    
    /**
     * Default constructor for CourseInfo class.
     */
    public CourseInfo() {}
    
    /**
     * Constructs a new CourseInfo with the specified details.
     * 
     * @param courseId The unique ID of the course
     * @param courseName The name of the course
     * @param courseCode The unique code for the course
     */
    public CourseInfo(int courseId, String courseName, String courseCode) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.isActive = true;
    }
    
    /**
     * Gets the unique identifier for this course.
     * 
     * @return The course ID
     */
    public int getCourseId() {
        return courseId;
    }
    
    /**
     * Sets the unique identifier for this course.
     * 
     * @param courseId The course ID to set
     */
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    /**
     * Gets the name of the course.
     * 
     * @return The course name
     */
    public String getCourseName() {
        return courseName;
    }
    
    /**
     * Sets the name of the course.
     * 
     * @param courseName The course name to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    /**
     * Gets the unique code for this course.
     * 
     * @return The course code
     */
    public String getCourseCode() {
        return courseCode;
    }
    
    /**
     * Sets the unique code for this course.
     * 
     * @param courseCode The course code to set
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    /**
     * Gets the ID of the instructor for this course.
     * 
     * @return The instructor ID
     */
    public int getInstructorId() {
        return instructorId;
    }
    
    /**
     * Sets the ID of the instructor for this course.
     * 
     * @param instructorId The instructor ID to set
     */
    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }
    
    /**
     * Gets the name of the instructor for this course.
     * 
     * @return The instructor name
     */
    public String getInstructorName() {
        return instructorName;
    }
    
    /**
     * Sets the name of the instructor for this course.
     * 
     * @param instructorName The instructor name to set
     */
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
    
    /**
     * Gets the description of the course.
     * 
     * @return The course description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description of the course.
     * 
     * @param description The course description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Gets the creation timestamp for this course.
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the creation timestamp for this course.
     * 
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Checks if the course is active.
     * 
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Sets the active status of the course.
     * 
     * @param active true if active, false otherwise
     */
    public void setActive(boolean active) {
        isActive = active;
    }
    
    /**
     * Gets the list of students enrolled in this course.
     * 
     * @return List of enrolled students
     */
    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }
    
    /**
     * Sets the list of students enrolled in this course.
     * 
     * @param enrolledStudents List of students to set
     */
    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }
    
    /**
     * Returns a string representation of the CourseInfo object.
     * 
     * @return A string containing the course name and code
     */
    @Override
    public String toString() {
        return courseName + " (" + courseCode + ")";
    }
} 