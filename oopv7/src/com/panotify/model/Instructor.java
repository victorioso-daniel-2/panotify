package com.panotify.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an instructor user in the PaNotify system.
 * Extends the base User class with instructor-specific attributes and behaviors,
 * such as created courses and exams.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class Instructor extends User {
    /** List of courses created by the instructor */
    private List<CourseInfo> createdCourses;
    
    /** List of exams created by the instructor */
    private List<Exam> createdExams;

    /**
     * Default constructor for Instructor.
     * Initializes empty lists for created courses and exams.
     */
    public Instructor() {
        super();
        this.createdCourses = new ArrayList<>();
        this.createdExams = new ArrayList<>();
    }

    /**
     * Parameterized constructor to create a new instructor with essential information.
     * Sets the account type to "instructor".
     * 
     * @param firstName Instructor's first name
     * @param lastName Instructor's last name
     * @param username Unique username for login
     * @param email Instructor's email address
     * @param phoneNumber Instructor's contact phone number
     * @param password Instructor's password
     * @param institution Educational institution the instructor belongs to
     * @param department Department or faculty within the institution
     */
    public Instructor(String firstName, String lastName, String username, String email, String phoneNumber, String password, String institution, String department) {
        super(firstName, lastName, username, email, phoneNumber, password, "instructor");
        this.setInstitution(institution);
        this.setDepartment(department);
        this.createdCourses = new ArrayList<>();
        this.createdExams = new ArrayList<>();
    }

    /**
     * Gets the list of courses created by the instructor.
     * 
     * @return List of created courses
     */
    public List<CourseInfo> getCreatedCourses() {
        return createdCourses;
    }

    /**
     * Sets the list of courses created by the instructor.
     * 
     * @param createdCourses List of created courses to set
     */
    public void setCreatedCourses(List<CourseInfo> createdCourses) {
        this.createdCourses = createdCourses;
    }

    /**
     * Gets the list of exams created by the instructor.
     * 
     * @return List of created exams
     */
    public List<Exam> getCreatedExams() {
        return createdExams;
    }

    /**
     * Sets the list of exams created by the instructor.
     * 
     * @param createdExams List of created exams to set
     */
    public void setCreatedExams(List<Exam> createdExams) {
        this.createdExams = createdExams;
    }

    /**
     * Returns a string representation of the Instructor object.
     * 
     * @return A string containing the instructor's key information
     */
    @Override
    public String toString() {
        return "Instructor{" +
                "userId=" + getUserId() +
                ", username='" + getUsername() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", institution='" + getInstitution() + '\'' +
                ", department='" + getDepartment() + '\'' +
                ", createdCourses=" + createdCourses.size() +
                '}';
    }
} 