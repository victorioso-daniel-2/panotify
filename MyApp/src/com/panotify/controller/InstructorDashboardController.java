package com.panotify.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class InstructorDashboardController {

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        // Navigate back to login or landing page
        Parent root = FXMLLoader.load(getClass().getResource("/com/panotify/view/InstructorLogin.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("PaNotify! - Instructor Login");
        stage.show();
    }

    @FXML
    private void handleManageCourses(ActionEvent event) {
        showAlert("Manage Courses", "This feature will be implemented soon.");
    }

    @FXML
    private void handleViewStudents(ActionEvent event) {
        showAlert("View Students", "This feature will be implemented soon.");
    }

    @FXML
    private void handleCreateExam(ActionEvent event) {
        showAlert("Create Exam", "This feature will be implemented soon.");
    }

    @FXML
    private void handleViewResults(ActionEvent event) {
        showAlert("View Results", "This feature will be implemented soon.");
    }

    @FXML
    private void handleHelp(ActionEvent event) {
        showAlert("Help", "For assistance, contact support@panotify.com");
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        showAlert("About", "PaNotify! v1.0 - Online Examination System");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
