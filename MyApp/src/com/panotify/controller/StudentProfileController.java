package com.panotify.controller;

import com.panotify.dao.UserDAO;
import com.panotify.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentProfileController implements Initializable {

    @FXML private Button userAvatarButton;
    @FXML private Button usernameButton;
    @FXML private Label userAvatar;
    @FXML private Label usernameLabel;
    
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField usernameField;
    @FXML private TextField accountTypeField;
    @FXML private TextField institutionField;
    @FXML private TextField departmentField;
    
    @FXML private VBox institutionContainer;
    @FXML private VBox departmentContainer;
    
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    
    private User currentUser;
    private UserDAO userDAO = new UserDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize will be empty as we need to wait for user data
    }
    
    public void initData(User user) {
        this.currentUser = user;
        
        // Set avatar to first letter of user's first name
        String firstNameLetter = user.getFullName().substring(0, 1).toUpperCase();
        userAvatar.setText(firstNameLetter);
        
        // Update UI with user information
        usernameLabel.setText(user.getFullName());
        
        // Populate form fields
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
        usernameField.setText(user.getUsername());
        accountTypeField.setText(user.getAccountType());
        
        // Show/hide instructor-specific fields
        if ("Instructor".equals(user.getAccountType())) {
            institutionContainer.setVisible(true);
            institutionContainer.setManaged(true);
            departmentContainer.setVisible(true);
            departmentContainer.setManaged(true);
            
            institutionField.setText(user.getInstitution());
            departmentField.setText(user.getDepartment());
        }
    }
    
    @FXML
    private void handleUserProfile(ActionEvent event) {
        // Already on profile page, so no navigation needed
        // Could refresh the profile data if needed
        if (currentUser != null) {
            initData(currentUser);
        }
    }
    
    @FXML
    private void handleSaveProfile(ActionEvent event) {
        // Validate inputs
        if (fullNameField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Full name and email are required fields.");
            return;
        }
        
        // Update the user object
        currentUser.setFullName(fullNameField.getText().trim());
        currentUser.setEmail(emailField.getText().trim());
        currentUser.setPhone(phoneField.getText().trim());
        
        // Update instructor fields if applicable
        if ("Instructor".equals(currentUser.getAccountType())) {
            currentUser.setInstitution(institutionField.getText().trim());
            currentUser.setDepartment(departmentField.getText().trim());
        }
        
        try {
            // Save the changes to the database
            boolean updated = userDAO.updateUser(currentUser);
            
            if (updated) {
                // Update the UI to show the changes
                usernameLabel.setText(currentUser.getFullName());
                
                // Set avatar to first letter of updated name
                String firstNameLetter = currentUser.getFullName().substring(0, 1).toUpperCase();
                userAvatar.setText(firstNameLetter);
                
                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile information updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile information.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating your profile: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleChangePassword(ActionEvent event) {
        // Validate inputs
        if (currentPasswordField.getText().isEmpty() || 
            newPasswordField.getText().isEmpty() || 
            confirmPasswordField.getText().isEmpty()) {
            
            showAlert(Alert.AlertType.ERROR, "Error", "All password fields are required.");
            return;
        }
        
        // Check if new passwords match
        if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "New password and confirmation do not match.");
            return;
        }
        
        try {
            // Verify current password (this would be handled by UserDAO in a real implementation)
            User verifiedUser = userDAO.authenticateUser(currentUser.getUsername(), currentPasswordField.getText());
            
            if (verifiedUser == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Current password is incorrect.");
                return;
            }
            
            // Update the password
            boolean updated = userDAO.updatePassword(currentUser.getId(), newPasswordField.getText());
            
            if (updated) {
                // Clear password fields
                currentPasswordField.clear();
                newPasswordField.clear();
                confirmPasswordField.clear();
                
                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update password.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating your password: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDashboard(ActionEvent event) {
        navigateTo("StudentDashboard.fxml", event);
    }

    @FXML
    private void handleMyExams(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Navigation", "Navigating to My Exams page");
        // In a real app, this would navigate to the My Exams page
    }

    @FXML
    private void handleResults(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Navigation", "Navigating to Results page");
        // In a real app, this would navigate to the Results page
    }
    
    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/panotify/view/" + fxmlFile));
            Parent root = loader.load();
            
            // Pass the current user to the next controller
            if (loader.getController() instanceof StudentDashboardController) {
                ((StudentDashboardController) loader.getController()).initData(currentUser);
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to navigate: " + e.getMessage());
        }
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}