package com.panotify.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.panotify.util.DatabaseUtil;
import java.sql.*;

public class ManageCourseController {

    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, Integer> colId;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, String> colCode;
    @FXML private TextField txtCourseName;
    @FXML private TextField txtCourseCode;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> data.getValue().courseIdProperty().asObject());
        colName.setCellValueFactory(data -> data.getValue().courseNameProperty());
        colCode.setCellValueFactory(data -> data.getValue().courseCodeProperty());

        loadCourses();

        courseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtCourseName.setText(newVal.getCourseName());
                txtCourseCode.setText(newVal.getCourseCode());
            }
        });
    }

    private void loadCourses() {
        courseList.clear();
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Courses")) {

            while (rs.next()) {
                courseList.add(new Course(
                    rs.getInt("id"),
                    rs.getString("course_name"),
                    rs.getString("course_code")
                ));
            }

            courseTable.setItems(courseList);
        } catch (SQLException e) {
            showAlert("Error loading courses: " + e.getMessage());
        }
    }

    @FXML
    private void addCourse() {
        String name = txtCourseName.getText().trim();
        String code = txtCourseCode.getText().trim();

        if (name.isEmpty() || code.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        String query = "INSERT INTO Courses (course_name, course_code) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, code);
            stmt.executeUpdate();
            loadCourses();
            clearFields();
        } catch (SQLException e) {
            showAlert("Error adding course: " + e.getMessage());
        }
    }

    @FXML
    private void updateCourse() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a course to update.");
            return;
        }

        String name = txtCourseName.getText().trim();
        String code = txtCourseCode.getText().trim();

        String query = "UPDATE Courses SET course_name = ?, course_code = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, code);
            stmt.setInt(3, selected.getCourseId());
            stmt.executeUpdate();
            loadCourses();
            clearFields();
        } catch (SQLException e) {
            showAlert("Error updating course: " + e.getMessage());
        }
    }

    @FXML
    private void deleteCourse() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a course to delete.");
            return;
        }

        String query = "DELETE FROM Courses WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, selected.getCourseId());
            stmt.executeUpdate();
            loadCourses();
            clearFields();
        } catch (SQLException e) {
            showAlert("Error deleting course: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtCourseName.clear();
        txtCourseCode.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
