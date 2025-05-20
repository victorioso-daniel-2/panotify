package com.panotify.controller;

import com.panotify.dao.UserDAO;
import com.panotify.model.User;
import com.panotify.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {

    @FXML private Label registrationTypeLabel;
    @FXML private ComboBox<String> accountTypeComboBox;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField phoneField;
    @FXML private TextField institutionField;
    @FXML private TextField departmentField;
    @FXML private Label institutionLabel;
    @FXML private Label departmentLabel;
    @FXML private RowConstraints institutionRow;
    @FXML private RowConstraints departmentRow;

    private String accountType;
    private UserDAO userDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the account type combo box
        accountTypeComboBox.setItems(FXCollections.observableArrayList("Student", "Instructor"));
        
        // Add listener to the combo box to show/hide instructor-specific fields
        accountTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                accountType = newVal;
                updateFormFields(newVal);
            }
        });
        
        // Default selection (can be determined by previous selection in landing page)
        accountTypeComboBox.getSelectionModel().selectFirst();
        
        // Initialize the UserDAO
        userDAO = new UserDAO();
    }

    private void updateFormFields(String accountType) {
        boolean isInstructor = "Instructor".equals(accountType);
        
        // Show/hide instructor-specific fields
        institutionLabel.setVisible(isInstructor);
        departmentLabel.setVisible(isInstructor);
        institutionField.setVisible(isInstructor);
        departmentField.setVisible(isInstructor);
        institutionRow.setMaxHeight(isInstructor ? -1 : 0);
        institutionRow.setPrefHeight(isInstructor ? 40 : 0);
        departmentRow.setMaxHeight(isInstructor ? -1 : 0);
        departmentRow.setPrefHeight(isInstructor ? 40 : 0);
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        // Validate form fields
        if (!validateForm()) {
            return;
        }
        
        try {
            // Check if username already exists
            if (DatabaseUtil.usernameExists(usernameField.getText().trim())) {
                showAlert("Registration Error", "Username already exists. Please choose a different username.");
                return;
            }
            
            // Check if email already exists
            if (DatabaseUtil.emailExists(emailField.getText().trim())) {
                showAlert("Registration Error", "Email already exists. Please use a different email address.");
                return;
            }
            
            // Create a new User object
            User user = new User(
                fullNameField.getText().trim(),
                emailField.getText().trim(),
                usernameField.getText().trim(),
                passwordField.getText(),
                phoneField.getText().trim(),
                accountType,
                "Instructor".equals(accountType) ? institutionField.getText().trim() : null,
                "Instructor".equals(accountType) ? departmentField.getText().trim() : null
            );
            
            // Register the user in the database
            int userId = userDAO.registerUser(user);
            
            if (userId > 0) {
                // Registration successful
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("Your " + accountType.toLowerCase() + " account has been created successfully!");
                alert.showAndWait();
                
                // Return to landing page
                navigateToLanding(event);
            } else {
                // Registration failed
                showAlert("Registration Error", "Could not complete registration. Please try again later.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Registration Error", "Could not complete registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            // Return to landing page
            navigateToLanding(event);
        } catch (Exception e) {
            showAlert("Navigation Error", "Could not return to landing page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHelp(ActionEvent event) {
        showAlert("Help", "Registration Help:\n\n" +
                "1. Select your account type (Student or Instructor)\n" +
                "2. Fill out all required fields\n" +
                "3. Make sure your passwords match\n" +
                "4. Click Register to create your account");
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        showAlert("About PaNotify!", 
                "PaNotify! is a comprehensive online examination system designed to facilitate secure test-taking, " +
                "question bank management, automatic grading, and detailed reporting for educational institutions.");
    }

    private boolean validateForm() {
        // Basic validation
        if (fullNameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter your full name.");
            return false;
        }
        
        if (emailField.getText().trim().isEmpty() || !isValidEmail(emailField.getText().trim())) {
            showAlert("Validation Error", "Please enter a valid email address.");
            return false;
        }
        
        if (usernameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter a username.");
            return false;
        }
        
        if (passwordField.getText().isEmpty()) {
            showAlert("Validation Error", "Please enter a password.");
            return false;
        }
        
        if (passwordField.getText().length() < 8) {
            showAlert("Validation Error", "Password must be at least 8 characters long.");
            return false;
        }
        
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert("Validation Error", "Passwords do not match.");
            return false;
        }
        
        // Instructor-specific validation
        if ("Instructor".equals(accountType)) {
            if (institutionField.getText().trim().isEmpty()) {
                showAlert("Validation Error", "Please enter your institution name.");
                return false;
            }
            
            if (departmentField.getText().trim().isEmpty()) {
                showAlert("Validation Error", "Please enter your department.");
                return false;
            }
        }
        
        return true;
    }

    private boolean isValidEmail(String email) {
        // Simple email validation
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private void navigateToLanding(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/LandingPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("PaNotify! - Online Examination System");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}