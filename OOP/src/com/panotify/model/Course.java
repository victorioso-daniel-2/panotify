package com.panotify.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a course in the PaNotify system.
 * A course is created by an instructor and can have multiple enrolled students.
 * Each course can contain multiple exams and tracks student enrollments.
 */
public class Course {
    private int courseId;
    private String courseName;
    private String courseCode;
    private int instructorId;
    private String instructorName;
    private LocalDateTime createdAt;
    private List<Exam> exams;
    private List<CourseEnrollment> enrollments;
    
    /**
     * Default constructor for Course class.
     * Initializes empty lists for exams and enrollments.
     */
    public Course() {
        this.exams = new ArrayList<>();
        this.enrollments = new ArrayList<>();
    }
    
    /**
     * Constructs a new Course with the specified details.
     * 
     * @param courseName The name of the course
     * @param courseCode The unique code for the course
     * @param instructorId The ID of the instructor teaching the course
     */
    public Course(String courseName, String courseCode, int instructorId) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.instructorId = instructorId;
        this.createdAt = LocalDateTime.now();
        this.exams = new ArrayList<>();
        this.enrollments = new ArrayList<>();
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
     * This code is used by students to enroll in the course.
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
     * Gets the ID of the instructor teaching this course.
     * 
     * @return The instructor's ID
     */
    public int getInstructorId() {
        return instructorId;
    }
    
    /**
     * Sets the ID of the instructor teaching this course.
     * 
     * @param instructorId The instructor's ID to set
     */
    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }
    
    /**
     * Gets the name of the instructor teaching this course.
     * 
     * @return The instructor's name
     */
    public String getInstructorName() {
        return instructorName;
    }
    
    /**
     * Sets the name of the instructor teaching this course.
     * 
     * @param instructorName The instructor's name to set
     */
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
    
    /**
     * Gets the timestamp when this course was created.
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the timestamp when this course was created.
     * 
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Gets the list of exams in this course.
     * 
     * @return List of exams
     */
    public List<Exam> getExams() {
        return exams;
    }
    
    /**
     * Sets the list of exams for this course.
     * 
     * @param exams List of exams to set
     */
    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }
    
    /**
     * Gets the list of student enrollments in this course.
     * 
     * @return List of course enrollments
     */
    public List<CourseEnrollment> getEnrollments() {
        return enrollments;
    }
    
    /**
     * Sets the list of student enrollments for this course.
     * 
     * @param enrollments List of course enrollments to set
     */
    public void setEnrollments(List<CourseEnrollment> enrollments) {
        this.enrollments = enrollments;
    }
    
    /**
     * Adds a new exam to this course.
     * 
     * @param exam The exam to add
     */
    public void addExam(Exam exam) {
        this.exams.add(exam);
    }
    
    /**
     * Adds a new student enrollment to this course.
     * 
     * @param enrollment The enrollment to add
     */
    public void addEnrollment(CourseEnrollment enrollment) {
        this.enrollments.add(enrollment);
    }
    
    /**
     * Returns a string representation of the Course object.
     * Format: "courseName (courseCode)"
     * 
     * @return A string containing the course name and code
     */
    @Override
    public String toString() {
        return courseName + " (" + courseCode + ")";
    }
} 