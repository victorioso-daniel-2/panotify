package com.panotify.model;

import java.util.List;

/**
 * Represents an instructor user in the PaNotify system.
 * Extends the base User class with instructor-specific functionality.
 * Manages created courses and exams for the instructor.
 */
public class Instructor extends User {
    private List<CourseInfo> createdCourses;
    private List<Exam> createdExams;
    
    /**
     * Default constructor for Instructor class.
     * Sets account type to "instructor".
     */
    public Instructor() {
        super();
        setAccountType("instructor");
    }
    
    /**
     * Constructs a new Instructor with the specified details.
     * Splits the full name into first and last name components.
     * 
     * @param fullName The instructor's full name
     * @param username The instructor's username
     * @param email The instructor's email address
     * @param phoneNumber The instructor's phone number
     * @param password The instructor's password
     * @param institution The instructor's institution
     * @param department The instructor's department
     */
    public Instructor(String fullName, String username, String email, String phoneNumber, String password, String institution, String department) {
        super(
            fullName.split(" ", 2)[0],
            fullName.split(" ", 2).length > 1 ? fullName.split(" ", 2)[1] : "",
            username,
            email,
            phoneNumber,
            password,
            "instructor"
        );
        setInstitution(institution);
        setDepartment(department);
    }
    
    /**
     * Gets the list of courses created by this instructor.
     * 
     * @return List of CourseInfo objects representing created courses
     */
    public List<CourseInfo> getCreatedCourses() {
        return createdCourses;
    }
    
    /**
     * Sets the list of courses created by this instructor.
     * 
     * @param createdCourses List of CourseInfo objects to set
     */
    public void setCreatedCourses(List<CourseInfo> createdCourses) {
        this.createdCourses = createdCourses;
    }
    
    /**
     * Gets the list of exams created by this instructor.
     * 
     * @return List of Exam objects representing created exams
     */
    public List<Exam> getCreatedExams() {
        return createdExams;
    }
    
    /**
     * Sets the list of exams created by this instructor.
     * 
     * @param createdExams List of Exam objects to set
     */
    public void setCreatedExams(List<Exam> createdExams) {
        this.createdExams = createdExams;
    }
    
    /**
     * Returns a string representation of the Instructor object.
     * Includes user ID, full name, username, email, phone number,
     * institution, and department.
     * 
     * @return A string containing the instructor's details
     */
    @Override
    public String toString() {
        return "Instructor{" +
                "userId=" + getUserId() +
                ", fullName='" + getFullName() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", institution='" + getInstitution() + '\'' +
                ", department='" + getDepartment() + '\'' +
                '}';
    }
}