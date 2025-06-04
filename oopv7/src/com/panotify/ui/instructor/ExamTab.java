package com.panotify.ui.instructor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.panotify.model.Course;
import com.panotify.model.Exam;
import com.panotify.model.Instructor;
import com.panotify.service.CourseService;
import com.panotify.service.ExamService;
import com.panotify.ui.UIUtils;

/**
 * Tab for creating and managing exams
 * Provides interface for instructors to create new exams, set exam parameters,
 * and add questions to exams
 */
public class ExamTab implements InstructorTab {
    private Instructor instructor;
    private CourseService courseService;
    private ExamService examService;
    private InstructorDashboard dashboard;
    private VBox content;
    private Course selectedCourse = null;
    
    /**
     * Creates a new ExamTab with the necessary services and data
     * 
     * @param instructor the logged-in instructor user
     * @param courseService service for course-related operations
     * @param examService service for exam-related operations
     * @param dashboard reference to the parent dashboard
     */
    public ExamTab(Instructor instructor, CourseService courseService, ExamService examService, InstructorDashboard dashboard) {
        this.instructor = instructor;
        this.courseService = courseService;
        this.examService = examService;
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
     * Refreshes the content and rebuilds the exam form
     */
    @Override
    public void onNavigatedTo() {
        content.getChildren().clear();
        VBox examForm = createExamForm();
        content.getChildren().add(examForm);
    }
    
    /**
     * Creates the exam creation form UI
     * Includes fields for course selection, exam title, duration, and deadline
     * 
     * @return a VBox containing the exam form elements
     */
    private VBox createExamForm() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 10;");
        
        // Header
        Label headerLabel = new Label("Create New Exam");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #222222;");
        
        // Create form fields
        VBox formFields = new VBox(15);
        formFields.setPadding(new Insets(20, 0, 20, 0));
        
        // Course selector
        Label courseLabel = new Label("Course *");
        courseLabel.setStyle("-fx-font-weight: bold;");
        
        ComboBox<Course> courseComboBox = new ComboBox<>();
        courseComboBox.setPrefWidth(400);
        courseComboBox.setPromptText("Select a course");
        
        // Custom cell factory to display course name
        courseComboBox.setCellFactory(param -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCourseName() + " (" + item.getCourseCode() + ")");
                }
            }
        });
        
        // Custom string converter for the selected value display
        courseComboBox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                if (course == null) {
                    return null;
                }
                return course.getCourseName() + " (" + course.getCourseCode() + ")";
            }
            
            @Override
            public Course fromString(String string) {
                return null; // Not needed for this use case
            }
        });
        
        // Error message label for course
        Label courseErrorLabel = new Label("Please select a course");
        courseErrorLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 12px;");
        courseErrorLabel.setVisible(false);
        
        try {
            // Fetch courses from database for the current instructor
            List<Course> courses = courseService.getCoursesByInstructor(instructor.getUserId());
            
            if (courses != null && !courses.isEmpty()) {
                courseComboBox.getItems().addAll(courses);
                
                // Set the selected course if it was passed from another view
                if (selectedCourse != null) {
                    for (Course course : courseComboBox.getItems()) {
                        if (course.getCourseId() == selectedCourse.getCourseId()) {
                            courseComboBox.setValue(course);
                            break;
                        }
                    }
                    // Reset the selected course after using it
                    selectedCourse = null;
                }
            } else {
                // No courses - show message and button to create course
                Label noCourseLabel = new Label("You need to create a course before creating an exam.");
                noCourseLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #dc3545;");
                
                Button createCourseBtn = new Button("Go to My Courses");
                createCourseBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                createCourseBtn.setOnAction(e -> dashboard.navigateToMyCourses());
                
                VBox messageBox = new VBox(10, noCourseLabel, createCourseBtn);
                messageBox.setAlignment(Pos.CENTER);
                
                return new VBox(20, headerLabel, messageBox);
            }
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not fetch courses: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Exam title field
        Label titleLabel = new Label("Exam Title *");
        titleLabel.setStyle("-fx-font-weight: bold;");
        TextField titleField = new TextField();
        titleField.setPromptText("Enter exam title");
        titleField.setPrefWidth(400);
        
        // Error message label for title
        Label titleErrorLabel = new Label("Please enter an exam title");
        titleErrorLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 12px;");
        titleErrorLabel.setVisible(false);
        
        // Duration field
        Label durationLabel = new Label("Duration (minutes) *");
        durationLabel.setStyle("-fx-font-weight: bold;");
        TextField durationField = new TextField();
        durationField.setPromptText("Enter duration in minutes");
        durationField.setPrefWidth(200);
        
        // Error message label for duration
        Label durationErrorLabel = new Label("Please enter a valid duration");
        durationErrorLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 12px;");
        durationErrorLabel.setVisible(false);
        
        // Restrict to numeric input
        durationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                durationField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        // Date picker for deadline
        Label deadlineLabel = new Label("Deadline (Optional)");
        deadlineLabel.setStyle("-fx-font-weight: bold;");
        
        HBox dateTimeBox = new HBox(10);
        dateTimeBox.setAlignment(Pos.CENTER_LEFT);
        
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select date");
        
        // Time format toggle
        ToggleGroup timeFormatGroup = new ToggleGroup();
        RadioButton format24hr = new RadioButton("24hr");
        RadioButton format12hr = new RadioButton("12hr");
        format24hr.setToggleGroup(timeFormatGroup);
        format12hr.setToggleGroup(timeFormatGroup);
        format24hr.setSelected(true); // Default to 24hr format
        
        HBox timeFormatBox = new HBox(10);
        timeFormatBox.getChildren().addAll(format24hr, format12hr);
        
        // Hour selector (will be updated based on format)
        ComboBox<String> hourComboBox = new ComboBox<>();
        // Initialize with 24hr format
        for (int i = 0; i < 24; i++) {
            hourComboBox.getItems().add(String.format("%02d", i));
        }
        hourComboBox.setValue("23"); // Default to 11 PM
        
        Label colonLabel = new Label(":");
        
        ComboBox<String> minuteComboBox = new ComboBox<>();
        for (int i = 0; i < 60; i += 5) {
            minuteComboBox.getItems().add(String.format("%02d", i));
        }
        minuteComboBox.setValue("59");
        
        // AM/PM selector (only visible in 12hr mode)
        ComboBox<String> amPmComboBox = new ComboBox<>();
        amPmComboBox.getItems().addAll("AM", "PM");
        amPmComboBox.setValue("PM");
        amPmComboBox.setVisible(false);
        
        // Add time components to the dateTimeBox
        dateTimeBox.getChildren().addAll(
            datePicker, 
            hourComboBox, 
            colonLabel, 
            minuteComboBox, 
            amPmComboBox, 
            new Label("  Format:"), 
            timeFormatBox
        );
        
        // Toggle between 12hr and 24hr formats
        timeFormatGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            boolean is24Hour = format24hr.isSelected();
            
            // Update hour values and visibility of AM/PM
            updateHourComboBox(hourComboBox, is24Hour);
            amPmComboBox.setVisible(!is24Hour);
            
            // Convert the current time when switching formats
            convertTimeFormat(hourComboBox, amPmComboBox, is24Hour);
        });
        
        // Publish checkbox
        CheckBox publishCheckBox = new CheckBox("Publish exam immediately");
        publishCheckBox.setSelected(false);
        
        // Add fields to form
        formFields.getChildren().addAll(
            courseLabel, courseComboBox, courseErrorLabel,
            titleLabel, titleField, titleErrorLabel,
            durationLabel, durationField, durationErrorLabel,
            deadlineLabel, dateTimeBox,
            publishCheckBox
        );
        
        // Create button actions
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
        cancelBtn.setOnAction(e -> dashboard.navigateToMyCourses());
        
        Button createBtn = new Button("Create Exam");
        createBtn.setStyle("-fx-background-color: #00d355; -fx-text-fill: white;");
        createBtn.setOnAction(e -> {
            // Reset error messages
            courseErrorLabel.setVisible(false);
            titleErrorLabel.setVisible(false);
            durationErrorLabel.setVisible(false);
            
            // Validate inputs
            boolean isValid = true;
            
            Course course = courseComboBox.getValue();
            if (course == null) {
                courseErrorLabel.setVisible(true);
                isValid = false;
            }
            
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                titleErrorLabel.setVisible(true);
                isValid = false;
            }
            
            String durationStr = durationField.getText().trim();
            int duration = 0;
            if (durationStr.isEmpty()) {
                durationErrorLabel.setVisible(true);
                isValid = false;
            } else {
                try {
                    duration = Integer.parseInt(durationStr);
                    if (duration <= 0) {
                        durationErrorLabel.setText("Duration must be a positive number");
                        durationErrorLabel.setVisible(true);
                        isValid = false;
                    }
                } catch (NumberFormatException ex) {
                    durationErrorLabel.setText("Invalid duration. Please enter a valid number");
                    durationErrorLabel.setVisible(true);
                    isValid = false;
                }
            }
            
            if (!isValid) {
                return;
            }
            
            try {
                // Create a new exam object
                Exam exam = new Exam();
                exam.setCourseId(course.getCourseId());
                exam.setInstructorId(instructor.getUserId()); // Explicitly set instructor ID
                exam.setTitle(title);
                exam.setDurationMinutes(duration);
                exam.setPublished(publishCheckBox.isSelected());
                
                // Set deadline if date is picked
                if (datePicker.getValue() != null) {
                    int hour;
                    if (format24hr.isSelected()) {
                        // 24-hour format - parse directly
                        hour = Integer.parseInt(hourComboBox.getValue());
                    } else {
                        // 12-hour format - convert to 24-hour
                        hour = Integer.parseInt(hourComboBox.getValue());
                        if (amPmComboBox.getValue().equals("PM") && hour < 12) {
                            hour += 12;
                        } else if (amPmComboBox.getValue().equals("AM") && hour == 12) {
                            hour = 0;
                        }
                    }
                    
                    LocalTime time = LocalTime.of(
                        hour,
                        Integer.parseInt(minuteComboBox.getValue())
                    );
                    LocalDateTime deadline = LocalDateTime.of(datePicker.getValue(), time);
                    exam.setDeadline(deadline);
                }
                
                // Save to database
                boolean success = examService.createExam(exam);
                
                if (success) {
                    UIUtils.showSuccessAlert("Success", "Exam created successfully!");
                    
                    // Clear the form
                    titleField.clear();
                    durationField.clear();
                    datePicker.setValue(null);
                    
                    // Navigate to My Courses
                    dashboard.navigateToMyCourses();
                } else {
                    UIUtils.showErrorAlert("Error", "Failed to create exam. Please try again.");
                }
            } catch (Exception ex) {
                UIUtils.showErrorAlert("Error", "An error occurred: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        buttonBox.getChildren().addAll(cancelBtn, createBtn);
        
        // Add all components to the content
        content.getChildren().addAll(headerLabel, formFields, buttonBox);
        return content;
    }
    
    // Helper method to update the hour ComboBox based on time format
    private void updateHourComboBox(ComboBox<String> hourComboBox, boolean is24Hour) {
        String currentValue = hourComboBox.getValue();
        hourComboBox.getItems().clear();
        
        if (is24Hour) {
            // 24-hour format (0-23)
            for (int i = 0; i < 24; i++) {
                hourComboBox.getItems().add(String.format("%02d", i));
            }
            // Set a valid default if current value is null or empty
            if (currentValue == null || currentValue.isEmpty()) {
                hourComboBox.setValue("23");
            } else {
                // Try to keep the selected hour if possible
                if (hourComboBox.getItems().contains(currentValue)) {
                    hourComboBox.setValue(currentValue);
                } else {
                    hourComboBox.setValue("23");
                }
            }
        } else {
            // 12-hour format (1-12)
            for (int i = 1; i <= 12; i++) {
                hourComboBox.getItems().add(String.format("%02d", i));
            }
            // Set a valid default if current value is null or empty
            if (currentValue == null || currentValue.isEmpty()) {
                hourComboBox.setValue("11");
            } else {
                // Try to keep the selected hour if possible
                if (hourComboBox.getItems().contains(currentValue)) {
                    hourComboBox.setValue(currentValue);
                } else {
                    hourComboBox.setValue("11");
                }
            }
        }
    }
    
    // Helper method to convert time between 12-hour and 24-hour formats
    private void convertTimeFormat(ComboBox<String> hourComboBox, ComboBox<String> amPmComboBox, boolean to24Hour) {
        try {
            int currentHour = Integer.parseInt(hourComboBox.getValue());
            
            if (to24Hour) {
                // Convert from 12-hour to 24-hour
                if (amPmComboBox.getValue().equals("PM") && currentHour < 12) {
                    // PM and not 12 -> add 12 (e.g., 1 PM -> 13)
                    hourComboBox.setValue(String.format("%02d", currentHour + 12));
                } else if (amPmComboBox.getValue().equals("AM") && currentHour == 12) {
                    // 12 AM -> 00
                    hourComboBox.setValue("00");
                } else {
                    // Otherwise, use same hour with leading zero
                    hourComboBox.setValue(String.format("%02d", currentHour));
                }
            } else {
                // Convert from 24-hour to 12-hour
                if (currentHour == 0) {
                    // 00 -> 12 AM
                    hourComboBox.setValue("12");
                    amPmComboBox.setValue("AM");
                } else if (currentHour < 12) {
                    // 01-11 -> 1-11 AM
                    hourComboBox.setValue(String.format("%02d", currentHour));
                    amPmComboBox.setValue("AM");
                } else if (currentHour == 12) {
                    // 12 -> 12 PM
                    hourComboBox.setValue("12");
                    amPmComboBox.setValue("PM");
                } else {
                    // 13-23 -> 1-11 PM
                    hourComboBox.setValue(String.format("%02d", currentHour - 12));
                    amPmComboBox.setValue("PM");
                }
            }
        } catch (NumberFormatException e) {
            // Handle parsing error - set default value
            if (to24Hour) {
                hourComboBox.setValue("23");
            } else {
                hourComboBox.setValue("11");
                amPmComboBox.setValue("PM");
            }
        }
    }

    public void setSelectedCourse(Course course) {
        if (course != null) {
            // Store the course to be selected when the tab is shown
            this.selectedCourse = course;
        }
    }
} 