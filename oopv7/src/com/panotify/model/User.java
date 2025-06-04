package com.panotify.model;

import java.time.LocalDateTime;

/**
 * Base user model representing a user in the PaNotify system.
 * This class serves as the parent class for specific user types like Student and Instructor.
 * It contains common user attributes and behaviors.
 * 
 * @author PaNotify Team
 * @version 1.0
 */
public class User {
    /** Unique identifier for the user */
    private int userId;
    
    /** User's first name */
    private String firstName;
    
    /** User's last name */
    private String lastName;
    
    /** Unique username for login */
    private String username;
    
    /** User's contact phone number */
    private String phoneNumber;
    
    /** User's email address */
    private String email;
    
    /** User's password (hashed) */
    private String password;
    
    /** Type of account (e.g., "student", "instructor") */
    private String accountType;
    
    /** Educational institution the user belongs to */
    private String institution;
    
    /** Department or faculty within the institution */
    private String department;
    
    /** Timestamp when the user account was created */
    private LocalDateTime createdAt;

    /**
     * Default constructor for User.
     */
    public User() {}

    /**
     * Parameterized constructor to create a new user with essential information.
     * 
     * @param firstName User's first name
     * @param lastName User's last name
     * @param username Unique username for login
     * @param email User's email address
     * @param phoneNumber User's contact phone number
     * @param password User's password
     * @param accountType Type of account (e.g., "student", "instructor")
     */
    public User(String firstName, String lastName, String username, String email, String phoneNumber, String password, String accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.accountType = accountType;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Gets the user's ID.
     * 
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user's ID.
     * 
     * @param userId The user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the user's first name.
     * 
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     * 
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     * 
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     * 
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's username.
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the user's username.
     * 
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the user's phone number.
     * 
     * @return The phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's phone number.
     * 
     * @param phoneNumber The phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the user's email address.
     * 
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     * 
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user's password.
     * 
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     * 
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's account type.
     * 
     * @return The account type
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * Sets the user's account type.
     * 
     * @param accountType The account type to set
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * Gets the user's institution.
     * 
     * @return The institution
     */
    public String getInstitution() {
        return institution;
    }

    /**
     * Sets the user's institution.
     * 
     * @param institution The institution to set
     */
    public void setInstitution(String institution) {
        this.institution = institution;
    }

    /**
     * Gets the user's department.
     * 
     * @return The department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the user's department.
     * 
     * @param department The department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the timestamp when the user account was created.
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the timestamp when the user account was created.
     * 
     * @param timestamp The creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime timestamp) {
        this.createdAt = timestamp;
    }

    /**
     * Gets the user's full name by combining first and last name.
     * 
     * @return The full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Sets the user's first and last name from a full name string.
     * 
     * @param fullName The full name to parse
     */
    public void setFullName(String fullName) {
        String[] parts = fullName.split(" ", 2);
        this.firstName = parts[0];
        this.lastName = parts.length > 1 ? parts[1] : "";
    }

    /**
     * Returns a string representation of the User object.
     * 
     * @return A string containing the user's key information
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", accountType='" + accountType + '\'' +
                '}';
    }
} 