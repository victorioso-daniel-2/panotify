package com.panotify.controller;

import com.panotify.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentDashboardController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label userInfoLabel;
    
    private User currentUser;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize dashboard UI components
    }
    
    public void initData(User user) {
        this.currentUser = user;
        
        // Update UI with user information
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getFullName() + "!");
            userInfoLabel.setText("Username: " + user.getUsername() + "\nEmail: " + user.getEmail());
        }
    }

    @FXML
    private void handleViewExams(ActionEvent event) {
        // Navigate to exams view
        showAlert("View Exams", "Exam viewing functionality will be implemented in the future.");
    }
    
    @FXML
    private void handleViewResults(ActionEvent event) {
        // Navigate to results view
        showAlert("View Results", "Result viewing functionality will be implemented in the future.");
    }
    
    @FXML
    private void handleViewProfile(ActionEvent event) {
        // Navigate to profile view
        showAlert("View Profile", "Profile viewing functionality will be implemented in the future.");
    }
    
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Return to landing page
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/LandingPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("PaNotify! - Online Examination System");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not return to landing page: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleHelp(ActionEvent event) {
        showAlert("Help", "Student Dashboard Help:\n\n" +
                "1. View Exams - See your upcoming and available exams\n" +
                "2. View Results - Check your exam results and feedback\n" +
                "3. View Profile - Update your personal information\n" +
                "4. Logout - Exit your account");
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