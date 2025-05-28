package com.panotify.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Represents a user in the PaNotify system.
 * This is the base class for both students and instructors.
 * Contains common user information such as personal details and authentication credentials.
 */
public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String email;
    private String password;
    private String accountType;
    private String institution;
    private String department;
    private LocalDateTime createdAt;
    
    /**
     * Default constructor for User class.
     */
    public User() {}
    
    /**
     * Constructs a new User with the specified details.
     * 
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param username The user's chosen username
     * @param email The user's email address
     * @param phoneNumber The user's phone number
     * @param password The user's password (should be hashed before storage)
     * @param accountType The type of account ("Student" or "Instructor")
     */
    public User(String firstName, String lastName, String username, String email, String phoneNumber, String password, String accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.accountType = accountType;
    }
    
    /**
     * Gets the unique identifier for this user.
     * 
     * @return The user's ID
     */
    public int getUserId() {
        return userId;
    }
    
    /**
     * Sets the unique identifier for this user.
     * 
     * @param userId The user's ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    /**
     * Gets the user's first name.
     * 
     * @return The user's first name
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
     * @return The user's last name
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
     * @return The password (hashed)
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Sets the user's password.
     * 
     * @param password The password to set (should be hashed)
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Gets the user's account type.
     * 
     * @return The account type ("Student" or "Instructor")
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
     * @return The institution name
     */
    public String getInstitution() {
        return institution;
    }
    
    /**
     * Sets the user's institution.
     * 
     * @param institution The institution name to set
     */
    public void setInstitution(String institution) {
        this.institution = institution;
    }
    
    /**
     * Gets the user's department.
     * 
     * @return The department name
     */
    public String getDepartment() {
        return department;
    }
    
    /**
     * Sets the user's department.
     * 
     * @param department The department name to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }
    
    /**
     * Gets the timestamp when this user account was created.
     * 
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Sets the timestamp when this user account was created.
     * 
     * @param timestamp The creation timestamp to set
     */
    public void setCreatedAt(LocalDateTime timestamp) {
        this.createdAt = timestamp;
    }
    
    /**
     * Gets the user's full name by combining first and last name.
     * 
     * @return The full name as "firstName lastName"
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Sets the user's full name by splitting it into first and last name.
     * 
     * @param fullName The full name to split and set
     */
    public void setFullName(String fullName) {
        String[] parts = fullName.split(" ", 2);
        this.firstName = parts[0];
        this.lastName = parts.length > 1 ? parts[1] : "";
    }
    
    /**
     * Returns a string representation of the User object.
     * 
     * @return A string containing all user details
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", accountType='" + accountType + '\'' +
                ", institution='" + institution + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}