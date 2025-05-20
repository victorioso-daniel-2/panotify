package com.panotify.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;

import java.io.IOException;

public class LandingPageController {

	@FXML
    private void handleStudentLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/StudentLogin.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Student Login - PaNotify!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not navigate to student login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTeacherLogin(ActionEvent event) {
        // Implement teacher login navigation
        System.out.println("Teacher login requested");
        // This would typically load a teacher login page
        // similar to student login but with the appropriate FXML file
    }

    @FXML
    private void handleRegistration(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/Registration.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Student Registration - PaNotify!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not navigate to registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHelp(MouseEvent event) {
        showAlert("Help", "PaNotify! Help:\n\n" +
                "• Student Login: Click 'Student' to access your student account\n" +
                "• Teacher Login: Click 'Teacher' to access your teacher account\n" +
                "• Register: Click 'Register now' if you don't have an account\n\n" +
                "For more assistance, please contact support.");
    }

    @FXML
    private void handleContact(MouseEvent event) {
        showAlert("Contact Us", "PaNotify! Support:\n\n" +
                "Email: support@panotify.com\n" +
                "Phone: (555) 123-4567\n" +
                "Hours: Monday-Friday, 9am-5pm");
    }

    @FXML
    private void handleAbout(MouseEvent event) {
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