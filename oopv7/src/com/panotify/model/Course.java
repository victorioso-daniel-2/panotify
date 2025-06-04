package com.panotify.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a course in the PaNotify system.
 * A course contains information about the course itself, its instructor, 
 * associated exams, and student enrollments.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class Course {
    /** Unique identifier for the course */
    private int courseId;
    
    /** Name of the course */
    private String courseName;
    
    /** Unique code for the course (e.g., "CS101") */
    private String courseCode;
    
    /** ID of the instructor teaching the course */
    private int instructorId;
    
    /** Full name of the instructor teaching the course */
    private String instructorName;
    
    /** Timestamp when the course was created */
    private LocalDateTime createdAt;
    
    /** Course description or syllabus */
    private String description;
    
    /** List of exams associated with this course */
    private List<Exam> exams;
    
    /** List of student enrollments in this course */
    private List<CourseEnrollment> enrollments;

    /**
     * Default constructor for Course.
     * Initializes empty lists for exams and enrollments.
     */
    public Course() {
        this.exams = new ArrayList<>();
        this.enrollments = new ArrayList<>();
    }

    /**
     * Parameterized constructor to create a new course with essential information.
     * 
     * @param courseName Name of the course
     * @param courseCode Unique code for the course
     * @param instructorId ID of the instructor teaching the course
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
     * Gets the course code.
     * 
     * @return The course code
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Sets the course code.
     * 
     * @param courseCode The course code to set
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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
     * Gets the instructor's full name.
     * 
     * @return The instructor's name
     */
    public String getInstructorName() {
        return instructorName;
    }

    /**
     * Sets the instructor's full name.
     * 
     * @param instructorName The instructor's name to set
     */
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    /**
     * Gets the timestamp when the course was created.
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when the course was created.
     * 
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the course description.
     * 
     * @return The course description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the course description.
     * 
     * @param description The course description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the list of exams associated with this course.
     * 
     * @return List of exams
     */
    public List<Exam> getExams() {
        return exams;
    }

    /**
     * Sets the list of exams associated with this course.
     * 
     * @param exams List of exams to set
     */
    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    /**
     * Gets the list of student enrollments in this course.
     * 
     * @return List of enrollments
     */
    public List<CourseEnrollment> getEnrollments() {
        return enrollments;
    }

    /**
     * Sets the list of student enrollments in this course.
     * 
     * @param enrollments List of enrollments to set
     */
    public void setEnrollments(List<CourseEnrollment> enrollments) {
        this.enrollments = enrollments;
    }

    /**
     * Adds an exam to the course if it's not already present.
     * 
     * @param exam The exam to add
     */
    public void addExam(Exam exam) {
        if (!exams.contains(exam)) {
            exams.add(exam);
        }
    }

    /**
     * Adds a student enrollment to the course if it's not already present.
     * 
     * @param enrollment The enrollment to add
     */
    public void addEnrollment(CourseEnrollment enrollment) {
        if (!enrollments.contains(enrollment)) {
            enrollments.add(enrollment);
        }
    }

    /**
     * Returns a string representation of the Course object.
     * 
     * @return A string containing the course's key information
     */
    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", instructorName='" + instructorName + '\'' +
                ", exams=" + (exams != null ? exams.size() : 0) +
                ", enrollments=" + (enrollments != null ? enrollments.size() : 0) +
                ", description='" + description + '\'' +
                '}';
    }
} 