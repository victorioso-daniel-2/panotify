package com.panotify.ui.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;

import java.util.List;

import com.panotify.model.Course;
import com.panotify.model.Student;
import com.panotify.service.CourseService;
import com.panotify.ui.UIUtils;

/**
 * Tab for displaying and managing student course enrollments
 * Provides interface for viewing enrolled courses, course details,
 * and enrolling in new courses
 */
public class CoursesTab implements StudentTab {
    private Student student;
    private CourseService courseService;
    private StudentDashboard dashboard;
    private VBox content;
    
    /**
     * Creates a new CoursesTab with the necessary services and data
     * 
     * @param student the logged-in student user
     * @param courseService service for course-related operations
     * @param dashboard reference to the parent dashboard
     */
    public CoursesTab(Student student, CourseService courseService, StudentDashboard dashboard) {
        this.student = student;
        this.courseService = courseService;
        this.dashboard = dashboard;
        this.content = new VBox();
        content.setPadding(new Insets(15));
        content.setStyle("-fx-background-color: rgba(211, 211, 211, 0.5); -fx-background-radius: 15;");
    }
    
    /**
     * Gets the content pane for this tab
     * 
     * @return the VBox containing this tab's content
     */
    @Override
    public VBox getContent() {
        return content;
    }
    
    /**
     * Called when this tab is navigated to
     * Refreshes the content and rebuilds the courses view
     */
    @Override
    public void onNavigatedTo() {
        content.getChildren().clear();
        content.getChildren().add(createCourseContent());
    }
    
    /**
     * Creates the course content view
     * Sets up the UI with course list and enrollment button
     * 
     * @return a VBox containing the course content view
     */
    private VBox createCourseContent() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        // Header
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label headerLabel = new Label("My Courses");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button enrollBtn = new Button("Enroll in Course");
        enrollBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        enrollBtn.setOnAction(e -> showEnrollCourseDialog());
        
        headerBox.getChildren().addAll(headerLabel, spacer, enrollBtn);
        
        // Course cards container
        VBox coursesContainer = new VBox(15);
        
        try {
            // Get enrolled courses
            List<Course> courses = courseService.getEnrolledCourses(student.getUserId());
            
            if (courses != null && !courses.isEmpty()) {
                // Add course cards
                for (Course course : courses) {
                    coursesContainer.getChildren().add(createCourseCard(course));
                }
            } else {
                // No courses message
                VBox noCoursesBox = new VBox(10);
                noCoursesBox.setAlignment(Pos.CENTER);
                noCoursesBox.setPadding(new Insets(30));
                
                Label noCoursesLabel = new Label("You are not enrolled in any courses yet");
                noCoursesLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #6c757d;");
                
                Button findCourseBtn = new Button("Enroll in a Course");
                findCourseBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                findCourseBtn.setOnAction(e -> showEnrollCourseDialog());
                
                noCoursesBox.getChildren().addAll(noCoursesLabel, findCourseBtn);
                coursesContainer.getChildren().add(noCoursesBox);
            }
        } catch (Exception e) {
            // Error loading courses
            Label errorLabel = new Label("Error loading courses: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
            coursesContainer.getChildren().add(errorLabel);
            e.printStackTrace();
        }
        
        // Add components to container
        container.getChildren().addAll(headerBox, coursesContainer);
        
        return container;
    }
    
    /**
     * Creates a course card UI component for a specific course
     * Displays course information and provides action buttons
     * 
     * @param course the course to create a card for
     * @return an HBox containing the styled course card
     */
    private HBox createCourseCard(Course course) {
        HBox card = new HBox();
        card.setPadding(new Insets(15));
        card.setSpacing(20);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5; -fx-border-color: #e9ecef; -fx-border-radius: 5;");
        
        // Course info
        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(300);
        
        Label nameLabel = new Label(course.getCourseName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        Label codeLabel = new Label("Course Code: " + course.getCourseCode());
        codeLabel.setStyle("-fx-font-size: 14px;");
        
        Label instructorLabel = new Label("Instructor: " + course.getInstructorName());
        instructorLabel.setStyle("-fx-font-size: 14px;");
        
        infoBox.getChildren().addAll(nameLabel, codeLabel, instructorLabel);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Action buttons
        VBox actionsBox = new VBox(10);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button viewDetailsBtn = new Button("View Details");
        viewDetailsBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        viewDetailsBtn.setPrefWidth(120);
        viewDetailsBtn.setOnAction(e -> showCourseDetails(course));
        
        Button viewExamsBtn = new Button("View Exams");
        viewExamsBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        viewExamsBtn.setPrefWidth(120);
        viewExamsBtn.setOnAction(e -> viewCourseExams(course));
        
        actionsBox.getChildren().addAll(viewDetailsBtn, viewExamsBtn);
        
        // Add components to card
        card.getChildren().addAll(infoBox, spacer, actionsBox);
        
        return card;
    }
    
    /**
     * Navigates to the exams tab and filters exams for a specific course
     * 
     * @param course the course to view exams for
     */
    private void viewCourseExams(Course course) {
        // Navigate to exams tab and filter by this course
        dashboard.navigateToMyExams();
        // ExamsTab will need a method to filter by course
    }
    
    /**
     * Shows a dialog with detailed information about a course
     * Displays course details and list of enrolled students
     * 
     * @param course the course to show details for
     */
    private void showCourseDetails(Course course) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Course Details");
        dialog.setHeaderText(course.getCourseName());
        
        // Create content pane
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setPrefWidth(600);
        
        // Course info section
        Label courseCodeLabel = new Label("Course Code: " + course.getCourseCode());
        courseCodeLabel.setStyle("-fx-font-size: 14px;");
        
        Label instructorLabel = new Label("Instructor: " + course.getInstructorName());
        instructorLabel.setStyle("-fx-font-size: 14px;");
        
        // Divider
        Separator separator = new Separator();
        
        // Enrolled Students section
        Label studentsLabel = new Label("Enrolled Students");
        studentsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        VBox studentsContainer = new VBox(5);
        
        try {
            // Get students for this course
            List<Student> enrolledStudents = dashboard.getUserService().getStudentsByCourse(course.getCourseId());
            
            if (enrolledStudents != null && !enrolledStudents.isEmpty()) {
                // Create simple table for students
                TableView<Student> studentsTable = new TableView<>();
                studentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                
                // Define columns
                TableColumn<Student, String> nameCol = new TableColumn<>("Name");
                nameCol.setCellValueFactory(data -> new SimpleStringProperty(
                        data.getValue().getFirstName() + " " + data.getValue().getLastName()));
                
                TableColumn<Student, String> emailCol = new TableColumn<>("Email");
                emailCol.setCellValueFactory(data -> new SimpleStringProperty(
                        data.getValue().getEmail()));
                
                studentsTable.getColumns().addAll(nameCol, emailCol);
                studentsTable.getItems().addAll(enrolledStudents);
                
                studentsContainer.getChildren().add(studentsTable);
            } else {
                Label noStudentsLabel = new Label("No other students enrolled in this course yet.");
                noStudentsLabel.setStyle("-fx-font-style: italic;");
                studentsContainer.getChildren().add(noStudentsLabel);
            }
        } catch (Exception e) {
            Label errorLabel = new Label("Error loading student list: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
            studentsContainer.getChildren().add(errorLabel);
            e.printStackTrace();
        }
        
        // View Exams button
        Button viewExamsBtn = new Button("View Exams");
        viewExamsBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        viewExamsBtn.setOnAction(e -> {
            dialog.close();
            viewCourseExams(course);
        });
        
        // Add components to content
        content.getChildren().addAll(
            courseCodeLabel,
            instructorLabel,
            separator,
            studentsLabel,
            studentsContainer,
            viewExamsBtn
        );
        
        dialog.getDialogPane().setContent(content);
        
        // Add close button
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        dialog.showAndWait();
    }
    
    /**
     * Shows a dialog for enrolling in a new course
     * Provides a form for entering a course code to enroll
     */
    private void showEnrollCourseDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Enroll in Course");
        dialog.setHeaderText("Enter the course code to enroll");
        
        // Set the button types
        ButtonType enrollButtonType = new ButtonType("Enroll", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(enrollButtonType, ButtonType.CANCEL);
        
        // Create the course code label and field
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField courseCodeField = new TextField();
        courseCodeField.setPromptText("Course Code");
        
        grid.add(new Label("Course Code:"), 0, 0);
        grid.add(courseCodeField, 1, 0);
        
        // Enable/Disable enroll button depending on whether a course code was entered
        Node enrollButton = dialog.getDialogPane().lookupButton(enrollButtonType);
        enrollButton.setDisable(true);
        
        // Validate course code input
        courseCodeField.textProperty().addListener((observable, oldValue, newValue) -> {
            enrollButton.setDisable(newValue.trim().isEmpty());
        });
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the course code field by default
        courseCodeField.requestFocus();
        
        // Convert the result to a course code when the enroll button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enrollButtonType) {
                return courseCodeField.getText();
            }
            return null;
        });
        
        // Process the result
        dialog.showAndWait().ifPresent(courseCode -> {
            enrollInCourse(courseCode);
        });
    }
    
    /**
     * Enrolls the student in a course with the specified course code
     * Calls the course service to perform the enrollment and shows feedback
     * 
     * @param courseCode the code of the course to enroll in
     */
    private void enrollInCourse(String courseCode) {
        try {
            boolean success = courseService.enrollStudentInCourse(student.getUserId(), courseCode);
            
            if (success) {
                UIUtils.showSuccessAlert("Success", "Successfully enrolled in the course.");
                // Refresh courses view
                onNavigatedTo();
            } else {
                UIUtils.showErrorAlert("Error", "Failed to enroll in the course. Please check the course code and try again.");
            }
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 