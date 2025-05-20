package com.panotify.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.util.Optional;

public class LandingPageController {

    @FXML
    private void handleStudentLogin(ActionEvent event) {
        try {
            // Load the student login page with correct path
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/StudentLogin.fxml"));
            navigateToScene(event, root, "Student Login - PaNotify!");
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not load student login page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            // Load the registration page with correct path
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/Registration.fxml"));
            navigateToScene(event, root, "Registration - PaNotify!");
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not load registration page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInstructorLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/InstructorLogin.fxml"));
            navigateToScene(event, root, "Instructor Login - PaNotify!");
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not load instructor login page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHelp(ActionEvent event) {
        showAlert("Help", "Help documentation will be available soon.");
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        showAlert("About PaNotify!", 
                "PaNotify! is a comprehensive online examination system designed to facilitate secure test-taking, " +
                "question bank management, automatic grading, and detailed reporting for educational institutions.");
    }

    @FXML
    private void handleContact(ActionEvent event) {
        showAlert("Contact Information", "For support or inquiries, please contact support@panotify.com");
    }
    
    // Helper method to navigate to a new scene
    private void navigateToScene(ActionEvent event, Parent root, String title) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    
    // Helper method to show alerts
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}