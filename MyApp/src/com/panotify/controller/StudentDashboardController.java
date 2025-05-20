package com.panotify.controller;

import com.panotify.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentDashboardController implements Initializable {

    @FXML private Label usernameLabel;
    @FXML private Label welcomeLabel;
    @FXML private Label userInitialLabel;
    @FXML private Button userAvatarButton;
    
    // Upcoming Exams Table
    @FXML private TableView<ExamItem> upcomingExamsTable;
    @FXML private TableColumn<ExamItem, String> examTitleColumn;
    @FXML private TableColumn<ExamItem, String> courseColumn;
    @FXML private TableColumn<ExamItem, String> dateColumn;
    @FXML private TableColumn<ExamItem, String> timeColumn;
    @FXML private TableColumn<ExamItem, String> durationColumn;
    @FXML private TableColumn<ExamItem, String> statusColumn;
    @FXML private TableColumn<ExamItem, Void> actionColumn;
    
    // Recent Results Table
    @FXML private TableView<ResultItem> recentResultsTable;
    @FXML private TableColumn<ResultItem, String> resultExamColumn;
    @FXML private TableColumn<ResultItem, String> resultCourseColumn;
    @FXML private TableColumn<ResultItem, String> dateTakenColumn;
    @FXML private TableColumn<ResultItem, String> scoreColumn;
    @FXML private TableColumn<ResultItem, String> gradeColumn;
    @FXML private TableColumn<ResultItem, Void> resultActionColumn;
    
    private User currentUser;
    private ObservableList<ExamItem> upcomingExams = FXCollections.observableArrayList();
    private ObservableList<ResultItem> recentResults = FXCollections.observableArrayList();
    private boolean profileUpdated = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize UI components
        setupUpcomingExamsTable();
        setupRecentResultsTable();
        
        // Add sample data for preview purposes
        loadSampleData();
    }
    
    public void initData(User user) {
        this.currentUser = user;
        
        // Update UI with user information
        updateUserDisplay();
    }
    
    private void updateUserDisplay() {
        if (currentUser == null) return;
        
        String fullName = currentUser.getFullName();
        usernameLabel.setText(fullName);
        
        // Set the initial (first letter of the name)
        String initial = fullName.substring(0, 1).toUpperCase();
        userInitialLabel.setText(initial);
        
        // Update welcome message
        welcomeLabel.setText("Welcome back, " + fullName.split(" ")[0] + 
                            ". Here are your upcoming exams and recent results.");
        
        // In a real app, you would load exams and results specific to this user
    }
    
    private void setupUpcomingExamsTable() {
        // Set up columns
        examTitleColumn.setCellValueFactory(new PropertyValueFactory<>("examTitle"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Custom cell factory for the status column to apply styling
        statusColumn.setCellFactory(column -> {
            return new TableCell<ExamItem, String>() {
                @Override
                protected void updateItem(String status, boolean empty) {
                    super.updateItem(status, empty);
                    
                    if (status == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(status);
                        
                        // Apply styling based on status
                        if ("Upcoming".equals(status)) {
                            setStyle("-fx-background-color: #FFB74D; -fx-text-fill: white; " +
                                    "-fx-padding: 5; -fx-background-radius: 3;");
                        } else if ("Available".equals(status)) {
                            setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                                    "-fx-padding: 5; -fx-background-radius: 3;");
                        }
                    }
                }
            };
        });
        
        // Custom cell factory for the action column with button
        actionColumn.setCellFactory(createButtonCellFactory("Start Exam", "Available"));
        
        // Set data source
        upcomingExamsTable.setItems(upcomingExams);
    }
    
    private void setupRecentResultsTable() {
        // Set up columns
        resultExamColumn.setCellValueFactory(new PropertyValueFactory<>("examTitle"));
        resultCourseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        dateTakenColumn.setCellValueFactory(new PropertyValueFactory<>("dateTaken"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        
        // Custom cell factory for the action column with button
        resultActionColumn.setCellFactory(createButtonCellFactory("View", null));
        
        // Set data source
        recentResultsTable.setItems(recentResults);
    }
    
    private <T> Callback<TableColumn<T, Void>, TableCell<T, Void>> createButtonCellFactory(String buttonText, String requiredStatus) {
        return new Callback<>() {
            @Override
            public TableCell<T, Void> call(final TableColumn<T, Void> param) {
                return new TableCell<T, Void>() {
                    private final Button btn = new Button(buttonText);
                    
                    {
                        btn.getStyleClass().add("primary-button");
                        btn.setOnAction((ActionEvent event) -> {
                            T data = getTableView().getItems().get(getIndex());
                            handleActionButtonClick(data);
                        });
                    }
                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Only show button if no status restriction or status matches
                            if (requiredStatus == null || 
                                (getTableView().getItems().get(getIndex()) instanceof ExamItem && 
                                 requiredStatus.equals(((ExamItem) getTableView().getItems().get(getIndex())).getStatus()))) {
                                setGraphic(btn);
                            } else {
                                setGraphic(new Label("-"));
                            }
                        }
                    }
                };
            }
        };
    }
    
    private void handleActionButtonClick(Object data) {
        if (data instanceof ExamItem) {
            ExamItem exam = (ExamItem) data;
            showAlert(Alert.AlertType.INFORMATION, "Exam Action", "Starting exam: " + exam.getExamTitle());
            // In a real app, this would navigate to the exam page
        } else if (data instanceof ResultItem) {
            ResultItem result = (ResultItem) data;
            showAlert(Alert.AlertType.INFORMATION, "Result Action", "Viewing details for: " + result.getExamTitle());
            // In a real app, this would open the result details
        }
    }
    
    private void loadSampleData() {
        // Sample upcoming exams
        upcomingExams.add(new ExamItem("Midterm Examination", "CS101 - Introduction to Programming", 
                          "May 15, 2025", "10:00 AM", "120 mins", "Upcoming"));
        upcomingExams.add(new ExamItem("Quiz #3", "CS205 - Data Structures", 
                          "May 10, 2025", "2:00 PM", "60 mins", "Available"));
        
        // Sample recent results
        recentResults.add(new ResultItem("Quiz #2", "CS205 - Data Structures", 
                          "April 28, 2025", "85/100", "A"));
        recentResults.add(new ResultItem("Lab Test #1", "CS101 - Introduction to Programming", 
                          "April 20, 2025", "72/100", "B"));
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        // Already on dashboard page, no action needed
    }

    @FXML
    private void handleMyExams(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Navigation", "Navigating to My Exams page");
        // In a real app, this would navigate to the My Exams page using navigateTo method
    }

    @FXML
    private void handleResults(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Navigation", "Navigating to Results page");
        // In a real app, this would navigate to the Results page using navigateTo method
    }

    @FXML
    private void handleUserProfile(ActionEvent event) {
        // Use navigateTo to go to the profile page as a full screen page, not a dialog
        navigateTo("StudentProfile.fxml", event);
    }

    @FXML
    private void handleViewAllResults(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Navigation", "Viewing all results");
        // In a real app, this would navigate to the full Results page using navigateTo method
    }
    
    private void navigateTo(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/panotify/view/" + fxmlFile));
            Parent root = loader.load();
            
            // Pass the current user to the next controller if it implements the appropriate method
            if (loader.getController() instanceof StudentDashboardController) {
                ((StudentDashboardController) loader.getController()).initData(currentUser);
            } else if (loader.getController() instanceof StudentProfileController) {
                ((StudentProfileController) loader.getController()).initData(currentUser);
            }
            // Add other controller types as needed
            
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
    
    // Getter and setter methods for profile update status
    public boolean isProfileUpdated() {
        return profileUpdated;
    }
    
    public void setProfileUpdated(boolean profileUpdated) {
        this.profileUpdated = profileUpdated;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        updateUserDisplay();
    }
    
    // Inner class for Exam items
    public static class ExamItem {
        private final SimpleStringProperty examTitle;
        private final SimpleStringProperty course;
        private final SimpleStringProperty date;
        private final SimpleStringProperty time;
        private final SimpleStringProperty duration;
        private final SimpleStringProperty status;
        
        public ExamItem(String examTitle, String course, String date, String time, String duration, String status) {
            this.examTitle = new SimpleStringProperty(examTitle);
            this.course = new SimpleStringProperty(course);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.duration = new SimpleStringProperty(duration);
            this.status = new SimpleStringProperty(status);
        }
        
        public String getExamTitle() { return examTitle.get(); }
        public void setExamTitle(String value) { examTitle.set(value); }
        
        public String getCourse() { return course.get(); }
        public void setCourse(String value) { course.set(value); }
        
        public String getDate() { return date.get(); }
        public void setDate(String value) { date.set(value); }
        
        public String getTime() { return time.get(); }
        public void setTime(String value) { time.set(value); }
        
        public String getDuration() { return duration.get(); }
        public void setDuration(String value) { duration.set(value); }
        
        public String getStatus() { return status.get(); }
        public void setStatus(String value) { status.set(value); }
    }
    
    // Inner class for Result items
    public static class ResultItem {
        private final SimpleStringProperty examTitle;
        private final SimpleStringProperty course;
        private final SimpleStringProperty dateTaken;
        private final SimpleStringProperty score;
        private final SimpleStringProperty grade;
        
        public ResultItem(String examTitle, String course, String dateTaken, String score, String grade) {
            this.examTitle = new SimpleStringProperty(examTitle);
            this.course = new SimpleStringProperty(course);
            this.dateTaken = new SimpleStringProperty(dateTaken);
            this.score = new SimpleStringProperty(score);
            this.grade = new SimpleStringProperty(grade);
        }
        
        public String getExamTitle() { return examTitle.get(); }
        public void setExamTitle(String value) { examTitle.set(value); }
        
        public String getCourse() { return course.get(); }
        public void setCourse(String value) { course.set(value); }
        
        public String getDateTaken() { return dateTaken.get(); }
        public void setDateTaken(String value) { dateTaken.set(value); }
        
        public String getScore() { return score.get(); }
        public void setScore(String value) { score.set(value); }
        
        public String getGrade() { return grade.get(); }
        public void setGrade(String value) { grade.set(value); }
    }
}