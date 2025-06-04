package com.panotify.ui.instructor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import com.panotify.model.Course;
import com.panotify.model.Exam;
import com.panotify.model.Instructor;
import com.panotify.model.Student;
import com.panotify.model.Question;
import com.panotify.service.CourseService;
import com.panotify.service.ExamService;
import com.panotify.service.UserService;
import com.panotify.ui.UIUtils;

/**
 * Tab for managing instructor courses
 * Provides interface for viewing, creating, and managing courses,
 * including student enrollment and associated exams
 */
public class CoursesTab implements InstructorTab {
    private Instructor instructor;
    private UserService userService;
    private CourseService courseService;
    private ExamService examService;
    private InstructorDashboard dashboard;
    private VBox content;
    
    /**
     * Creates a new CoursesTab with the necessary services and data
     * 
     * @param instructor the logged-in instructor user
     * @param userService service for user-related operations
     * @param courseService service for course-related operations
     * @param examService service for exam-related operations
     * @param dashboard reference to the parent dashboard
     */
    public CoursesTab(Instructor instructor, UserService userService, CourseService courseService, 
                       ExamService examService, InstructorDashboard dashboard) {
        this.instructor = instructor;
        this.userService = userService;
        this.courseService = courseService;
        this.examService = examService;
        this.dashboard = dashboard;
        this.content = createCoursesContent();
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
     * Refreshes the course list to show latest data
     */
    @Override
    public void onNavigatedTo() {
        refreshCoursesView();
    }
    
    /**
     * Creates the initial courses view content
     * Sets up the layout with header, add course button, and course cards
     * 
     * @return a VBox containing the courses view elements
     */
    private VBox createCoursesContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(15));
        content.setStyle("-fx-background-color: rgba(211, 211, 211, 0.5); -fx-background-radius: 15;");
        
        // Header row with title and button
        HBox headerBox = new HBox(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        // Header
        Label headerLabel = new Label("My Courses");
        headerLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: black;");
        
        // Create button to add new course
        Button addCourseBtn = new Button("+ Add New Course");
        addCourseBtn.setStyle("-fx-background-color: #00d355; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5;");
        addCourseBtn.setPrefHeight(30);
        addCourseBtn.setOnAction(e -> showCreateCourseForm());
        
        // Add spacer to push button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        headerBox.getChildren().addAll(headerLabel, spacer, addCourseBtn);
        
        // Courses container
        VBox coursesContainer = new VBox(15);
        
        try {
            // Fetch courses from database for the current instructor
            List<Course> courses = courseService.getCoursesByInstructor(instructor.getUserId());
            
            if (courses != null && !courses.isEmpty()) {
                for (Course course : courses) {
                    HBox courseCard = createCourseCard(course);
                    coursesContainer.getChildren().add(courseCard);
                }
            } else {
                // No courses found
                Label noCoursesLabel = new Label("You haven't created any courses yet.");
                noCoursesLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");
                coursesContainer.getChildren().add(noCoursesLabel);
            }
        } catch (Exception e) {
            Label errorLabel = new Label("Error loading courses: " + e.getMessage());
            errorLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");
            coursesContainer.getChildren().add(errorLabel);
            e.printStackTrace();
        }
        
        // Scrollable container with transparent background
        ScrollPane scrollPane = new ScrollPane(coursesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        
        content.getChildren().addAll(headerBox, scrollPane);
        return content;
    }
    
    /**
     * Creates a card UI element for a course
     * Displays course details and action buttons
     * 
     * @param course the course to create a card for
     * @return an HBox containing the course card elements
     */
    private HBox createCourseCard(Course course) {
        // Create a clean white card with subtle shadow
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 1);");
        card.setPrefWidth(800);
        
        // Left side with course info
        VBox contentBox = new VBox(5);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(contentBox, Priority.ALWAYS);
        
        // Course name in bold
        Label nameLabel = new Label(course.getCourseName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");
        
        // Course code
        Label codeLabel = new Label("Course Code: " + course.getCourseCode());
        codeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        
        contentBox.getChildren().addAll(nameLabel, codeLabel);
        
        // Right side with buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        // View Students button (blue)
        Button viewStudentsBtn = new Button("View Students");
        viewStudentsBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-background-radius: 5;");
        viewStudentsBtn.setOnAction(e -> showCourseStudents(course));
        
        // View Exams button (orange)
        Button viewExamsBtn = new Button("View Exams");
        viewExamsBtn.setStyle("-fx-background-color: #fd7e14; -fx-text-fill: white; -fx-background-radius: 5;");
        viewExamsBtn.setOnAction(e -> showCourseExams(course));
        
        buttonBox.getChildren().addAll(viewStudentsBtn, viewExamsBtn);
        
        // Add a spacer to push the buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        card.getChildren().addAll(contentBox, spacer, buttonBox);
        return card;
    }
    
    /**
     * Displays a dialog showing students enrolled in a course
     * Shows a table of students with their information
     * 
     * @param course the course to show students for
     */
    private void showCourseStudents(Course course) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(dashboard.getPrimaryStage());
            dialogStage.setTitle("Students in " + course.getCourseName());
            dialogStage.setMinWidth(800);
            dialogStage.setMinHeight(500);
            
            VBox content = new VBox(15);
            content.setPadding(new Insets(20));
            
            Label titleLabel = new Label("Students in " + course.getCourseName());
            titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            
            // Get students for this course
            List<Student> students = userService.getStudentsByCourse(course.getCourseId());
            
            if (students == null || students.isEmpty()) {
                // No students enrolled
                Label noStudentsLabel = new Label("No students enrolled in this course yet.");
                noStudentsLabel.setStyle("-fx-font-size: 16px; -fx-font-style: italic;");
                
                VBox messageBox = new VBox(15, noStudentsLabel);
                messageBox.setAlignment(Pos.CENTER);
                messageBox.setPadding(new Insets(50, 0, 50, 0));
                
                content.getChildren().addAll(titleLabel, messageBox);
            } else {
                // Students found - display in a table
                TableView<Student> studentsTable = new TableView<>();
                studentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                
                // Define columns
                TableColumn<Student, String> nameCol = new TableColumn<>("Name");
                nameCol.setCellValueFactory(data -> new SimpleStringProperty(
                        data.getValue().getFirstName() + " " + data.getValue().getLastName()));
                nameCol.setPrefWidth(200);
                
                TableColumn<Student, String> emailCol = new TableColumn<>("Email");
                emailCol.setCellValueFactory(data -> new SimpleStringProperty(
                        data.getValue().getEmail()));
                emailCol.setPrefWidth(250);
                
                TableColumn<Student, String> idCol = new TableColumn<>("Student ID");
                idCol.setCellValueFactory(data -> new SimpleStringProperty(
                        data.getValue().getStudentId()));
                idCol.setPrefWidth(150);
                
                // Add columns to table - removed enrolledCol
                studentsTable.getColumns().addAll(nameCol, emailCol, idCol);
                
                // Add students to table
                studentsTable.getItems().addAll(students);
                
                // Student count
                Label countLabel = new Label(students.size() + " students enrolled");
                countLabel.setStyle("-fx-font-size: 14px;");
                
                // Add components to content
                content.getChildren().addAll(titleLabel, countLabel, studentsTable);
                
                // Set table to grow with window
                VBox.setVgrow(studentsTable, Priority.ALWAYS);
            }
            
            // Close button
            Button closeButton = new Button("Close");
            closeButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            closeButton.setOnAction(e -> dialogStage.close());
            
            HBox buttonBox = new HBox(closeButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            buttonBox.setPadding(new Insets(10, 0, 0, 0));
            
            content.getChildren().add(buttonBox);
            
            Scene scene = new Scene(content);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not display students: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showCourseExams(Course course) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(dashboard.getPrimaryStage());
            dialogStage.setTitle("Exams for " + course.getCourseName());
            dialogStage.setMinWidth(800);
            dialogStage.setMinHeight(500);
            
            VBox content = new VBox(15);
            content.setPadding(new Insets(20));
            
            // Header with title and add exam button
            HBox headerBox = new HBox(15);
            headerBox.setAlignment(Pos.CENTER_LEFT);
            
            Label titleLabel = new Label("Exams for " + course.getCourseName());
            titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            Button addExamBtn = new Button("+ Add Exam");
            addExamBtn.setStyle("-fx-background-color: #00d355; -fx-text-fill: white;");
            addExamBtn.setOnAction(e -> {
                dialogStage.close();
                // Navigate to the exam tab and pass the course
                dashboard.getExamTab().setSelectedCourse(course);
                dashboard.navigateToCreateExam();
            });
            
            headerBox.getChildren().addAll(titleLabel, spacer, addExamBtn);
            
            // Exams container - add ID for lookup
            VBox examsContainer = new VBox(15);
            examsContainer.setId("examsContainer"); // Add ID for lookup
            examsContainer.setPadding(new Insets(10));
            
            // Get exams for the course
            List<Exam> exams = examService.getExamsByCourse(course.getCourseId());
            
            if (exams != null && !exams.isEmpty()) {
                for (Exam exam : exams) {
                    HBox examCard = createExamCard(exam, dialogStage);
                    examsContainer.getChildren().add(examCard);
                }
            } else {
                // No exams found - show message with clickable link to create exam
                Label noExamsLabel = new Label("No exams found for this course.");
                noExamsLabel.setStyle("-fx-font-size: 18px; -fx-font-style: italic; -fx-text-fill: black;");
                
                Hyperlink createExamLink = new Hyperlink("Add one");
                createExamLink.setStyle("-fx-font-size: 18px; -fx-underline: true;");
                createExamLink.setOnAction(e -> {
                    dialogStage.close();
                    dashboard.getExamTab().setSelectedCourse(course);
                    dashboard.navigateToCreateExam();
                });
                
                HBox messageBox = new HBox(5);
                messageBox.setAlignment(Pos.CENTER);
                messageBox.getChildren().addAll(noExamsLabel, createExamLink);
                messageBox.setPadding(new Insets(30));
                
                examsContainer.getChildren().add(messageBox);
            }
            
            // Make exams scrollable
            ScrollPane scrollPane = new ScrollPane(examsContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
            
            // Close button
            Button closeButton = new Button("Close");
            closeButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            closeButton.setOnAction(e -> dialogStage.close());
            
            HBox buttonBox = new HBox(closeButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            buttonBox.setPadding(new Insets(10, 0, 0, 0));
            
            content.getChildren().addAll(headerBox, scrollPane, buttonBox);
            
            Scene scene = new Scene(content);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not display exams: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private HBox createExamCard(Exam exam, Stage parentStage) {
        HBox card = new HBox();
        card.setPadding(new Insets(15));
        card.setSpacing(15);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #adb5bd; -fx-border-radius: 5; -fx-border-width: 1.5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);");
        card.setPrefWidth(700);
        card.setUserData(exam);

        // Exam information
        VBox infoBox = new VBox(10); // Increased spacing
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        
        // Title and status
        HBox titleBox = new HBox(10);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        
        // Make sure the title is displayed prominently
        String titleText = exam.getTitle() != null && !exam.getTitle().isEmpty() ? exam.getTitle() : 
                          (exam.getExamTitle() != null && !exam.getExamTitle().isEmpty() ? exam.getExamTitle() : "Untitled Exam");
        
        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #0d6efd; -fx-padding: 5;"); // More prominent style
        
        Label statusLabel = new Label(exam.isPublished() ? "Published" : "Draft");
        statusLabel.setStyle(exam.isPublished() 
            ? "-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 3 10; -fx-background-radius: 5;" 
            : "-fx-background-color: #6c757d; -fx-text-fill: white; -fx-padding: 3 10; -fx-background-radius: 5;");
        
        titleBox.getChildren().addAll(titleLabel, statusLabel);
        
        // Details
        VBox detailsBox = new VBox(8); // Increased spacing
        
        Label durationLabel = new Label("Duration: " + exam.getDurationMinutes() + " minutes");
        durationLabel.setStyle("-fx-font-size: 14px;");
        
        String deadlineText = "Deadline: ";
        if (exam.getDeadline() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            deadlineText += exam.getDeadline().format(formatter);
        } else {
            deadlineText += "None";
        }
        
        Label deadlineLabel = new Label(deadlineText);
        deadlineLabel.setStyle("-fx-font-size: 14px;");
        
        detailsBox.getChildren().addAll(durationLabel, deadlineLabel);
        
        infoBox.getChildren().addAll(titleBox, detailsBox);
        
        // Action buttons in a vertical layout
        VBox buttonsBox = new VBox(10); // Increased spacing
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        buttonsBox.setMinWidth(150);
        
        // Edit button
        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-min-width: 120px;");
        editBtn.setOnAction(e -> showEditExamDialog(exam, parentStage));
        
        // Manage Questions button
        Button questionsBtn = new Button("Manage Questions");
        questionsBtn.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-min-width: 120px;");
        questionsBtn.setOnAction(e -> showManageQuestionsDialog(exam, parentStage));
        
        // Publish/Unpublish button
        Button publishBtn = new Button(exam.isPublished() ? "Unpublish" : "Publish");
        publishBtn.setStyle(exam.isPublished()
            ? "-fx-background-color: #ffc107; -fx-text-fill: black; -fx-min-width: 120px;"
            : "-fx-background-color: #28a745; -fx-text-fill: white; -fx-min-width: 120px;");
        publishBtn.setOnAction(e -> toggleExamPublishStatus(exam, parentStage));
        
        // Delete button
        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-min-width: 120px;");
        deleteBtn.setOnAction(e -> confirmDeleteExam(exam, parentStage));
        
        buttonsBox.getChildren().addAll(editBtn, questionsBtn, publishBtn, deleteBtn);
        
        card.getChildren().addAll(infoBox, buttonsBox);
        
        return card;
    }
    
    private void showEditExamDialog(Exam exam, Stage parentStage) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(parentStage);
            dialogStage.setTitle("Edit Exam");
            dialogStage.setMinWidth(600);
            dialogStage.setMinHeight(400);
            
            VBox content = new VBox(20);
            content.setPadding(new Insets(20));
            content.setStyle("-fx-background-color: white;");
            
            // Header
            Label headerLabel = new Label("Edit Exam");
            headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            
            // Form fields
            VBox formFields = new VBox(15);
            
            // Exam title
            Label titleLabel = new Label("Exam Title *");
            titleLabel.setStyle("-fx-font-weight: bold;");
            TextField titleField = new TextField(exam.getTitle());
            titleField.setPrefWidth(400);
            
            // Duration
            Label durationLabel = new Label("Duration (minutes) *");
            durationLabel.setStyle("-fx-font-weight: bold;");
            TextField durationField = new TextField(String.valueOf(exam.getDurationMinutes()));
            durationField.setPrefWidth(200);
            
            // Restrict to numeric input
            durationField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    durationField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
            
            // Deadline
            Label deadlineLabel = new Label("Deadline (Optional)");
            deadlineLabel.setStyle("-fx-font-weight: bold;");
            
            HBox dateTimeBox = new HBox(10);
            dateTimeBox.setAlignment(Pos.CENTER_LEFT);
            
            DatePicker datePicker = new DatePicker();
            if (exam.getDeadline() != null) {
                datePicker.setValue(exam.getDeadline().toLocalDate());
            }
            
            // Hour selector
            ComboBox<String> hourComboBox = new ComboBox<>();
            for (int i = 0; i < 24; i++) {
                hourComboBox.getItems().add(String.format("%02d", i));
            }
            
            Label colonLabel = new Label(":");
            
            ComboBox<String> minuteComboBox = new ComboBox<>();
            for (int i = 0; i < 60; i += 5) {
                minuteComboBox.getItems().add(String.format("%02d", i));
            }
            
            // Set current time values if deadline exists
            if (exam.getDeadline() != null) {
                hourComboBox.setValue(String.format("%02d", exam.getDeadline().getHour()));
                minuteComboBox.setValue(String.format("%02d", (exam.getDeadline().getMinute() / 5) * 5)); // Round to nearest 5
            } else {
                hourComboBox.setValue("23");
                minuteComboBox.setValue("59");
            }
            
            dateTimeBox.getChildren().addAll(datePicker, hourComboBox, colonLabel, minuteComboBox);
            
            // Buttons
            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            
            Button cancelButton = new Button("Cancel");
            cancelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            cancelButton.setOnAction(e -> dialogStage.close());
            
            Button saveButton = new Button("Save Changes");
            saveButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
            saveButton.setOnAction(e -> {
                try {
                    // Validate input
                    if (titleField.getText().trim().isEmpty()) {
                        UIUtils.showErrorAlert("Error", "Exam title cannot be empty");
                        return;
                    }
                    
                    if (durationField.getText().trim().isEmpty() || 
                        Integer.parseInt(durationField.getText().trim()) <= 0) {
                        UIUtils.showErrorAlert("Error", "Duration must be a positive number");
                        return;
                    }
                    
                    // Update exam object
                    exam.setTitle(titleField.getText().trim());
                    exam.setDurationMinutes(Integer.parseInt(durationField.getText().trim()));
                    
                    // Set deadline if provided
                    if (datePicker.getValue() != null) {
                        LocalDate date = datePicker.getValue();
                        LocalTime time = LocalTime.of(
                            Integer.parseInt(hourComboBox.getValue()),
                            Integer.parseInt(minuteComboBox.getValue())
                        );
                        exam.setDeadline(LocalDateTime.of(date, time));
                    } else {
                        exam.setDeadline(null);
                    }
                    
                    // Save to database
                    boolean success = examService.updateExam(exam);
                    
                    if (success) {
                        UIUtils.showSuccessAlert("Success", "Exam updated successfully");
                        dialogStage.close();
                        
                        // Refresh the exam list
                        parentStage.close();
                        showCourseExams(courseService.getCourseById(exam.getCourseId()));
                    } else {
                        UIUtils.showErrorAlert("Error", "Failed to update exam");
                    }
                } catch (Exception ex) {
                    UIUtils.showErrorAlert("Error", "An error occurred: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            
            buttonBox.getChildren().addAll(cancelButton, saveButton);
            
            // Add components to form
            formFields.getChildren().addAll(
                titleLabel, titleField,
                durationLabel, durationField,
                deadlineLabel, dateTimeBox
            );
            
            content.getChildren().addAll(headerLabel, formFields, buttonBox);
            
            Scene scene = new Scene(content);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not open edit dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showManageQuestionsDialog(Exam exam, Stage parentStage) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(parentStage);
            dialogStage.setTitle("Manage Questions - " + exam.getTitle());
            dialogStage.setMinWidth(900);
            dialogStage.setMinHeight(600);
            
            BorderPane mainLayout = new BorderPane();
            mainLayout.setPadding(new Insets(15));
            
            // Header
            Label headerLabel = new Label("Manage Questions for: " + exam.getTitle());
            headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            
            // Question list on the left
            VBox questionListPanel = new VBox(10);
            questionListPanel.setPadding(new Insets(10));
            questionListPanel.setMinWidth(250);
            questionListPanel.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5;");
            
            Label questionsLabel = new Label("Questions");
            questionsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            
            ListView<Question> questionListView = new ListView<>();
            questionListView.setPrefHeight(450);
            
            // Custom cell factory for questions
            questionListView.setCellFactory(param -> new ListCell<Question>() {
                @Override
                protected void updateItem(Question item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        String text = "Q" + (questionListView.getItems().indexOf(item) + 1) + ": " + 
                                     (item.getQuestionText().length() > 30 ? 
                                      item.getQuestionText().substring(0, 30) + "..." : 
                                      item.getQuestionText());
                        setText(text);
                    }
                }
            });
            
            // Load questions for the exam
            try {
                List<Question> questions = examService.getExamQuestions(exam.getExamId());
                questionListView.getItems().addAll(questions);
            } catch (SQLException e) {
                UIUtils.showErrorAlert("Error", "Could not load questions: " + e.getMessage());
                e.printStackTrace();
            }
            
            // Add question button
            Button addQuestionBtn = new Button("+ Add Question");
            addQuestionBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
            addQuestionBtn.setMaxWidth(Double.MAX_VALUE);
            
            questionListPanel.getChildren().addAll(questionsLabel, questionListView, addQuestionBtn);
            
            // Question detail panel on the right
            VBox questionDetailPanel = new VBox(15);
            questionDetailPanel.setPadding(new Insets(10));
            questionDetailPanel.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
            
            // Question type selection
            HBox typeSelectionBox = new HBox(15);
            typeSelectionBox.setAlignment(Pos.CENTER_LEFT);
            
            Label typeLabel = new Label("Question Type:");
            typeLabel.setStyle("-fx-font-weight: bold;");
            
            ToggleGroup typeGroup = new ToggleGroup();
            RadioButton multipleChoiceRadio = new RadioButton("Multiple Choice");
            RadioButton identificationRadio = new RadioButton("Identification");
            multipleChoiceRadio.setToggleGroup(typeGroup);
            identificationRadio.setToggleGroup(typeGroup);
            multipleChoiceRadio.setSelected(true);
            
            typeSelectionBox.getChildren().addAll(typeLabel, multipleChoiceRadio, identificationRadio);
            
            // Question text
            Label questionTextLabel = new Label("Question Text *");
            questionTextLabel.setStyle("-fx-font-weight: bold;");
            
            TextArea questionTextArea = new TextArea();
            questionTextArea.setPromptText("Enter your question here");
            questionTextArea.setPrefRowCount(3);
            questionTextArea.setWrapText(true);
            
            // Points
            Label pointsLabel = new Label("Points *");
            pointsLabel.setStyle("-fx-font-weight: bold;");
            
            TextField pointsField = new TextField("1");
            pointsField.setPrefWidth(100);
            pointsField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    pointsField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
            
            // Options section (for multiple choice)
            VBox optionsBox = new VBox(10);
            Label optionsLabel = new Label("Options *");
            optionsLabel.setStyle("-fx-font-weight: bold;");
            
            VBox optionsList = new VBox(5);
            
            // Add 4 default options
            for (int i = 0; i < 4; i++) {
                HBox optionBox = createOptionBox(optionsList, i == 0);
                optionsList.getChildren().add(optionBox);
            }
            
            Button addOptionBtn = new Button("+ Add Option");
            addOptionBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            
            optionsBox.getChildren().addAll(optionsLabel, optionsList, addOptionBtn);
            
            // Answer section (for identification)
            VBox answerBox = new VBox(10);
            Label answerLabel = new Label("Correct Answer *");
            answerLabel.setStyle("-fx-font-weight: bold;");
            
            TextField answerField = new TextField();
            answerField.setPromptText("Enter the correct answer");
            
            answerBox.getChildren().addAll(answerLabel, answerField);
            answerBox.setVisible(false);
            
            // Action buttons
            HBox actionButtonsBox = new HBox(10);
            actionButtonsBox.setAlignment(Pos.CENTER_RIGHT);
            
            Button deleteBtn = new Button("Delete Question");
            deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
            deleteBtn.setDisable(true);
            
            Button clearBtn = new Button("Clear");
            clearBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            
            Button saveBtn = new Button("Save Question");
            saveBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
            
            actionButtonsBox.getChildren().addAll(deleteBtn, clearBtn, saveBtn);
            
            questionDetailPanel.getChildren().addAll(
                typeSelectionBox,
                questionTextLabel, questionTextArea,
                pointsLabel, pointsField,
                optionsBox,
                answerBox,
                actionButtonsBox
            );
            
            // Bottom buttons
            HBox bottomButtonsBox = new HBox(10);
            bottomButtonsBox.setAlignment(Pos.CENTER_RIGHT);
            bottomButtonsBox.setPadding(new Insets(10, 0, 0, 0));
            
            Button closeButton = new Button("Close");
            closeButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            closeButton.setOnAction(e -> dialogStage.close());
            
            bottomButtonsBox.getChildren().add(closeButton);
            
            // Layout setup
            BorderPane.setMargin(headerLabel, new Insets(0, 0, 15, 0));
            
            SplitPane splitPane = new SplitPane();
            splitPane.getItems().addAll(questionListPanel, questionDetailPanel);
            splitPane.setDividerPositions(0.3);
            
            mainLayout.setTop(headerLabel);
            mainLayout.setCenter(splitPane);
            mainLayout.setBottom(bottomButtonsBox);
            
            // Event handlers
            
            // Toggle between multiple choice and identification
            multipleChoiceRadio.setOnAction(e -> {
                optionsBox.setVisible(true);
                answerBox.setVisible(false);
            });
            
            identificationRadio.setOnAction(e -> {
                optionsBox.setVisible(false);
                answerBox.setVisible(true);
            });
            
            // Add new option
            addOptionBtn.setOnAction(e -> {
                HBox optionBox = createOptionBox(optionsList, false);
                optionsList.getChildren().add(optionBox);
            });
            
            // Clear form
            clearBtn.setOnAction(e -> {
                questionTextArea.clear();
                pointsField.setText("1");
                multipleChoiceRadio.setSelected(true);
                optionsBox.setVisible(true);
                answerBox.setVisible(false);
                
                // Clear options
                optionsList.getChildren().clear();
                for (int i = 0; i < 4; i++) {
                    HBox optionBox = createOptionBox(optionsList, i == 0);
                    optionsList.getChildren().add(optionBox);
                }
                
                answerField.clear();
                questionListView.getSelectionModel().clearSelection();
                deleteBtn.setDisable(true);
            });
            
            // Select question from list
            questionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    questionTextArea.setText(newVal.getQuestionText());
                    pointsField.setText(String.valueOf(newVal.getPoints()));
                    
                    String options = newVal.getOptions();
                    if (options != null && !options.isEmpty()) {
                        // It's a multiple choice question
                        multipleChoiceRadio.setSelected(true);
                        optionsBox.setVisible(true);
                        answerBox.setVisible(false);
                        
                        // Parse options
                        String[] optionsArray = options.split("\\|");
                        int correctOption = newVal.getCorrectOption();
                        
                        // Clear existing options
                        optionsList.getChildren().clear();
                        
                        // Add options from the question
                        for (int i = 0; i < optionsArray.length; i++) {
                            HBox optionBox = createOptionBox(optionsList, i == correctOption);
                            TextField optionField = (TextField) optionBox.getChildren().get(1);
                            optionField.setText(optionsArray[i]);
                            optionsList.getChildren().add(optionBox);
                        }
                    } else {
                        // It's an identification question
                        identificationRadio.setSelected(true);
                        optionsBox.setVisible(false);
                        answerBox.setVisible(true);
                        answerField.setText(newVal.getCorrectAnswer());
                    }
                    
                    deleteBtn.setDisable(false);
                }
            });
            
            // Add new question
            addQuestionBtn.setOnAction(e -> {
                clearBtn.fire(); // Clear the form
            });
            
            // Delete question
            deleteBtn.setOnAction(e -> {
                Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
                if (selectedQuestion != null) {
                    boolean confirm = UIUtils.showConfirmationDialog(
                        "Delete Question",
                        "Are you sure you want to delete this question? This action cannot be undone.",
                        "Delete", "Cancel"
                    );
                    
                    if (confirm) {
                        try {
                            boolean success = examService.deleteQuestion(selectedQuestion.getQuestionId());
                            if (success) {
                                questionListView.getItems().remove(selectedQuestion);
                                clearBtn.fire(); // Clear the form
                                UIUtils.showSuccessAlert("Success", "Question deleted successfully");
                            } else {
                                UIUtils.showErrorAlert("Error", "Failed to delete question");
                            }
                        } catch (SQLException ex) {
                            UIUtils.showErrorAlert("Error", "An error occurred: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            });
            
            // Save question
            saveBtn.setOnAction(e -> {
                try {
                    // Validate input
                    if (questionTextArea.getText().trim().isEmpty()) {
                        UIUtils.showErrorAlert("Error", "Question text cannot be empty");
                        return;
                    }
                    
                    if (pointsField.getText().trim().isEmpty() || Integer.parseInt(pointsField.getText().trim()) <= 0) {
                        UIUtils.showErrorAlert("Error", "Points must be a positive number");
                        return;
                    }
                    
                    Question question;
                    Question selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
                    
                    if (selectedQuestion != null) {
                        // Editing existing question
                        question = selectedQuestion;
                    } else {
                        // Creating new question
                        question = new Question();
                        question.setExamId(exam.getExamId());
                    }
                    
                    question.setQuestionText(questionTextArea.getText().trim());
                    question.setPoints(Integer.parseInt(pointsField.getText().trim()));
                    
                    if (multipleChoiceRadio.isSelected()) {
                        // Multiple choice question
                        StringBuilder optionsBuilder = new StringBuilder();
                        int correctOptionIndex = -1;
                        
                        // Validate that we have at least 2 options
                        if (optionsList.getChildren().size() < 2) {
                            UIUtils.showErrorAlert("Error", "Multiple choice questions must have at least 2 options");
                            return;
                        }
                        
                        // Process each option
                        for (int i = 0; i < optionsList.getChildren().size(); i++) {
                            HBox optionBox = (HBox) optionsList.getChildren().get(i);
                            RadioButton radioBtn = (RadioButton) optionBox.getChildren().get(0);
                            TextField optionField = (TextField) optionBox.getChildren().get(1);
                            
                            String optionText = optionField.getText().trim();
                            if (optionText.isEmpty()) {
                                UIUtils.showErrorAlert("Error", "Option " + (i + 1) + " cannot be empty");
                                return;
                            }
                            
                            if (radioBtn.isSelected()) {
                                correctOptionIndex = i;
                            }
                            
                            optionsBuilder.append(optionText);
                            if (i < optionsList.getChildren().size() - 1) {
                                optionsBuilder.append("|");
                            }
                        }
                        
                        if (correctOptionIndex == -1) {
                            UIUtils.showErrorAlert("Error", "You must select a correct option");
                            return;
                        }
                        
                        question.setOptions(optionsBuilder.toString());
                        question.setCorrectOption(correctOptionIndex);
                    } else {
                        // Identification question
                        String answer = answerField.getText().trim();
                        if (answer.isEmpty()) {
                            UIUtils.showErrorAlert("Error", "Correct answer cannot be empty");
                            return;
                        }
                        
                        question.setOptions(answer);
                        question.setCorrectOption(0); // For identification, we store the answer in options and set correctOption to 0
                    }
                    
                    boolean success;
                    if (selectedQuestion != null) {
                        // Update existing question
                        success = examService.updateQuestion(question);
                        if (success) {
                            // Refresh the list
                            int selectedIndex = questionListView.getSelectionModel().getSelectedIndex();
                            questionListView.getItems().set(selectedIndex, question);
                            UIUtils.showSuccessAlert("Success", "Question updated successfully");
                        }
                    } else {
                        // Add new question
                        int questionId = examService.addQuestion(question);
                        if (questionId > 0) {
                            question.setQuestionId(questionId);
                            questionListView.getItems().add(question);
                            UIUtils.showSuccessAlert("Success", "Question added successfully");
                            clearBtn.fire(); // Clear the form for next question
                            success = true;
                        } else {
                            success = false;
                        }
                    }
                    
                    if (!success) {
                        UIUtils.showErrorAlert("Error", "Failed to save question");
                    }
                } catch (Exception ex) {
                    UIUtils.showErrorAlert("Error", "An error occurred: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            
            Scene scene = new Scene(mainLayout);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not open question management dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Helper method to create an option box for multiple choice questions
    private HBox createOptionBox(VBox parent, boolean isSelected) {
        HBox optionBox = new HBox(10);
        optionBox.setAlignment(Pos.CENTER_LEFT);
        
        ToggleGroup optionGroup = new ToggleGroup();
        // Find all radio buttons in the parent and add them to the group
        for (Node node : parent.getChildren()) {
            if (node instanceof HBox) {
                HBox box = (HBox) node;
                for (Node child : box.getChildren()) {
                    if (child instanceof RadioButton) {
                        RadioButton rb = (RadioButton) child;
                        rb.setToggleGroup(optionGroup);
                    }
                }
            }
        }
        
        RadioButton radioBtn = new RadioButton();
        radioBtn.setToggleGroup(optionGroup);
        radioBtn.setSelected(isSelected);
        
        TextField optionField = new TextField();
        optionField.setPromptText("Enter option text");
        optionField.setPrefWidth(300);
        
        Button removeBtn = new Button("×");
        removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #dc3545; -fx-font-weight: bold;");
        removeBtn.setOnAction(e -> {
            if (parent.getChildren().size() > 2) { // Ensure at least 2 options remain
                parent.getChildren().remove(optionBox);
            } else {
                UIUtils.showErrorAlert("Error", "Multiple choice questions must have at least 2 options");
            }
        });
        
        optionBox.getChildren().addAll(radioBtn, optionField, removeBtn);
        return optionBox;
    }
    
    private void toggleExamPublishStatus(Exam exam, Stage parentStage) {
        try {
            // Check if any students have submitted the exam before unpublishing
            if (exam.isPublished()) {
                boolean hasSubmissions = examService.hasAnySubmissions(exam.getExamId());
                if (hasSubmissions) {
                    UIUtils.showErrorAlert("Cannot Unpublish", 
                        "This exam cannot be unpublished because students have already submitted responses.");
                    return;
                }
            } else {
                // Before publishing, make sure the deadline is in the future or null
                if (exam.getDeadline() != null && exam.getDeadline().isBefore(LocalDateTime.now())) {
                    boolean confirm = UIUtils.showConfirmationDialog(
                        "Warning", 
                        "The deadline for this exam is in the past. Would you like to remove the deadline?",
                        "Remove Deadline", "Cancel");
                    
                    if (confirm) {
                        exam.setDeadline(null);
                    } else {
                        return;
                    }
                }
            }
            
            // Toggle publish status
            exam.setPublished(!exam.isPublished());
            boolean success = examService.updateExam(exam);
            
            if (success) {
                UIUtils.showSuccessAlert("Success", 
                    exam.isPublished() ? "Exam published successfully." : "Exam unpublished successfully.");
                
                // Update the button and status label in the current card
                for (Node node : parentStage.getScene().getRoot().lookupAll(".button")) {
                    if (node instanceof Button) {
                        Button button = (Button) node;
                        
                        // Find the parent HBox (card) that contains this button
                        Node parent = button.getParent();
                        while (parent != null && !(parent instanceof HBox)) {
                            parent = parent.getParent();
                        }
                        
                        // Check if this button belongs to the exam card we're updating
                        if (parent != null && parent instanceof HBox && parent.getUserData() == exam) {
                            if (button.getText().equals("Publish") || button.getText().equals("Unpublish")) {
                                button.setText(exam.isPublished() ? "Unpublish" : "Publish");
                                button.setStyle(exam.isPublished()
                                    ? "-fx-background-color: #ffc107; -fx-text-fill: black; -fx-min-width: 120px;"
                                    : "-fx-background-color: #28a745; -fx-text-fill: white; -fx-min-width: 120px;");
                                
                                // Also update the status label in the same card
                                for (Node labelNode : ((HBox) parent).lookupAll(".label")) {
                                    if (labelNode instanceof Label) {
                                        Label label = (Label) labelNode;
                                        if (label.getText().equals("Published") || label.getText().equals("Draft")) {
                                            label.setText(exam.isPublished() ? "Published" : "Draft");
                                            label.setStyle(exam.isPublished() 
                                                ? "-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 3;" 
                                                : "-fx-background-color: #6c757d; -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 3;");
                                            break;
                                        }
                                    }
                                }
                                
                                break;
                            }
                        }
                    }
                }
            } else {
                UIUtils.showErrorAlert("Error", "Failed to update exam status.");
            }
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void confirmDeleteExam(Exam exam, Stage parentStage) {
        boolean confirm = UIUtils.showConfirmationDialog("Confirm Delete", 
            "Are you sure you want to delete this exam? This action cannot be undone.",
            "Delete", "Cancel");
            
        if (confirm) {
            try {
                boolean success = examService.deleteExam(exam.getExamId());
                if (success) {
                    // Show success message immediately, don't close window yet
                    UIUtils.showSuccessAlert("Success", "Exam deleted successfully.");
                    
                    // Get the course ID 
                    int courseId = exam.getCourseId();
                    
                    // Remove the exam card from the UI
                    if (parentStage != null && parentStage.getScene() != null) {
                        VBox examsContainer = (VBox) parentStage.getScene().lookup("#examsContainer");
                        if (examsContainer != null) {
                            // Find and remove the card for this exam
                            examsContainer.getChildren().removeIf(node -> {
                                if (node instanceof HBox) {
                                    HBox card = (HBox) node;
                                    return card.getUserData() == exam;
                                }
                                return false;
                            });
                        }
                    }
                } else {
                    UIUtils.showErrorAlert("Error", "Failed to delete exam.");
                }
            } catch (Exception e) {
                UIUtils.showErrorAlert("Error", "An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void showCreateCourseForm() {
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(dashboard.getPrimaryStage());
            dialogStage.setTitle("Create New Course");
            dialogStage.setMinWidth(500);
            dialogStage.setMinHeight(300);
            
            VBox content = new VBox(15);
            content.setPadding(new Insets(20));
            content.setStyle("-fx-background-color: white;");
            
            Label headerLabel = new Label("Create New Course");
            headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            
            Label nameLabel = new Label("Course Name");
            nameLabel.setStyle("-fx-font-weight: bold;");
            
            TextField nameField = new TextField();
            nameField.setPromptText("Enter course name");
            
            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            
            Button cancelBtn = new Button("Cancel");
            cancelBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            cancelBtn.setOnAction(e -> dialogStage.close());
            
            Button createBtn = new Button("Create Course");
            createBtn.setStyle("-fx-background-color: #00d355; -fx-text-fill: white;");
            createBtn.setOnAction(e -> {
                String courseName = nameField.getText().trim();
                if (courseName.isEmpty()) {
                    UIUtils.showErrorAlert("Error", "Course name cannot be empty");
                    return;
                }
                
                // Create and save the course
                try {
                    Course course = new Course();
                    course.setCourseName(courseName);
                    String courseCode = generateRandomCourseCode();
                    course.setCourseCode(courseCode);
                    course.setInstructorId(instructor.getUserId());
                    course.setCreatedAt(LocalDateTime.now());
                    
                    boolean success = courseService.createCourse(course);
                    if (success) {
                        dialogStage.close();
                        
                        // Show confirmation dialog with the course details
                        showCourseCreatedDialog(courseName, courseCode);
                        
                        refreshCoursesView();
                    } else {
                        UIUtils.showErrorAlert("Error", "Failed to create course");
                    }
                } catch (Exception ex) {
                    UIUtils.showErrorAlert("Error", "Error creating course: " + ex.getMessage());
                }
            });
            
            buttonBox.getChildren().addAll(cancelBtn, createBtn);
            
            content.getChildren().addAll(headerLabel, nameLabel, nameField, buttonBox);
            
            Scene scene = new Scene(content);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not open create course form: " + e.getMessage());
        }
    }
    
    private void showCourseCreatedDialog(String courseName, String courseCode) {
        Stage confirmDialog = new Stage();
        confirmDialog.initModality(Modality.APPLICATION_MODAL);
        confirmDialog.initOwner(dashboard.getPrimaryStage());
        confirmDialog.setTitle("Course Created");
        confirmDialog.setMinWidth(400);
        confirmDialog.setMinHeight(250);
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: white;");
        
        Label successLabel = new Label("Course Created Successfully!");
        successLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #00d355;");
        
        VBox courseInfoBox = new VBox(10);
        courseInfoBox.setAlignment(Pos.CENTER);
        courseInfoBox.setPadding(new Insets(15));
        courseInfoBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5;");
        
        Label courseNameLabel = new Label("Course Name: " + courseName);
        courseNameLabel.setStyle("-fx-font-size: 16px;");
        
        Label courseCodeLabel = new Label("Course Code: " + courseCode);
        courseCodeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        courseInfoBox.getChildren().addAll(courseNameLabel, courseCodeLabel);
        
        Label noteLabel = new Label("Share this code with your students so they can enroll in your course.");
        noteLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d;");
        noteLabel.setWrapText(true);
        
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #00d355; -fx-text-fill: white;");
        closeButton.setOnAction(e -> confirmDialog.close());
        
        content.getChildren().addAll(successLabel, courseInfoBox, noteLabel, closeButton);
        
        Scene scene = new Scene(content);
        confirmDialog.setScene(scene);
        confirmDialog.showAndWait();
    }
    
    private String generateRandomCourseCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        java.util.Random random = new java.util.Random();
        
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(chars.length());
            codeBuilder.append(chars.charAt(index));
        }
        
        return codeBuilder.toString();
    }
    
    public void refreshCoursesView() {
        content.getChildren().clear();
        VBox newContent = createCoursesContent();
        
        // Create a copy of the children list to avoid concurrent modification
        List<Node> childrenCopy = new ArrayList<>(newContent.getChildren());
        
        // Copy content instead of replacing the panel
        for (Node child : childrenCopy) {
            content.getChildren().add(child);
        }
    }
} 