package com.panotify.controller;

import com.panotify.dao.UserDAO;
import com.panotify.model.User;
import com.panotify.util.DatabaseUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentLoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    
    private UserDAO userDAO;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the UserDAO
        userDAO = new UserDAO();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Basic validation
        if (username.isEmpty()) {
            showAlert("Login Error", "Please enter your username.");
            return;
        }
        
        if (password.isEmpty()) {
            showAlert("Login Error", "Please enter your password.");
            return;
        }
        
        try {
            // Authenticate user using the UserDAO
            User user = userDAO.authenticateUser(username, password);
            
            if (user != null && "Student".equals(user.getAccountType())) {
                // Successful login, show notification
                showAlert("Login Successful", "Welcome to PaNotify!, " + user.getFullName() + "!");
                
                // Navigate to student dashboard
                navigateToStudentDashboard(event, user);
            } else {
                showAlert("Login Failed", "Invalid username or password. Please try again.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Login Error", "Could not complete login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void navigateToStudentDashboard(ActionEvent event, User user) throws IOException {
        try {
            // Load the student dashboard and pass the user data
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/panotify/view/StudentDashboard.fxml"));
            Parent root = loader.load();
            
            // Get the controller and pass the user object
            StudentDashboardController dashboardController = loader.getController();
            dashboardController.initData(user);
            
            // Set up the scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Student Dashboard - PaNotify!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not navigate to dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Navigate back to landing page with correct path
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/LandingPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("PaNotify! - Online Examination System");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not navigate back: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        try {
            // Navigate to password reset page
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/PasswordReset.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Password Reset - PaNotify!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not navigate to password reset: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            // Navigate to registration page with correct path
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/Registration.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Student Registration - PaNotify!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not navigate to registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHelp(ActionEvent event) {
        showAlert("Help", "Student Login Help:\n\n" +
                "1. Enter your username\n" +
                "2. Enter your password\n" +
                "3. Click Login to access your account\n\n" +
                "If you forgot your password, click 'Reset it here'.\n" +
                "If you don't have an account yet, click 'Register'.");
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        showAlert("About PaNotify!", 
                "PaNotify! is a comprehensive online examination system designed to facilitate secure test-taking, " +
                "question bank management, automatic grading, and detailed reporting for educational institutions.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}