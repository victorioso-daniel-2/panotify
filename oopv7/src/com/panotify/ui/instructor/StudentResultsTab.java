package com.panotify.ui.instructor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import com.panotify.model.Course;
import com.panotify.model.Exam;
import com.panotify.model.Instructor;
import com.panotify.model.Report;
import com.panotify.model.Student;
import com.panotify.service.CourseService;
import com.panotify.service.ExamService;
import com.panotify.service.UserService;
import com.panotify.ui.UIUtils;

/**
 * Tab for viewing and analyzing student exam results
 * Provides interface for instructors to filter students by course,
 * view individual student performance, and examine detailed exam results
 */
public class StudentResultsTab implements InstructorTab {
    private Instructor instructor;
    private UserService userService;
    private CourseService courseService;
    private ExamService examService;
    private InstructorDashboard dashboard;
    private VBox content;
    
    /**
     * Creates a new StudentResultsTab with the necessary services and data
     * 
     * @param instructor the logged-in instructor user
     * @param userService service for user-related operations
     * @param courseService service for course-related operations
     * @param examService service for exam-related operations
     * @param dashboard reference to the parent dashboard
     */
    public StudentResultsTab(Instructor instructor, UserService userService, CourseService courseService, 
                             ExamService examService, InstructorDashboard dashboard) {
        this.instructor = instructor;
        this.userService = userService;
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
     * Refreshes the content and rebuilds the student results view
     */
    @Override
    public void onNavigatedTo() {
        content.getChildren().clear();
        VBox resultsView = createStudentResultsView();
        content.getChildren().add(resultsView);
    }
    
    /**
     * Creates the student results view
     * Sets up the UI with filters, table of students, and result details
     * 
     * @return a VBox containing the student results view
     */
    private VBox createStudentResultsView() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 10;");
        
        // Header
        Label headerLabel = new Label("Student Results");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #222222;");
        
        // Divider
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #e0e0e0;");
        
        // Filter section
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(10, 0, 10, 0));
        
        Label courseFilterLabel = new Label("Filter by Course:");
        courseFilterLabel.setStyle("-fx-font-weight: bold;");
        
        ComboBox<Course> courseFilterComboBox = new ComboBox<>();
        courseFilterComboBox.setPromptText("All Courses");
        courseFilterComboBox.setPrefWidth(250);
        
        // Add "All Courses" option
        Course allCourses = new Course();
        allCourses.setCourseId(-1); // Special ID for all courses
        allCourses.setCourseName("All Courses");
        allCourses.setCourseCode("ALL");
        courseFilterComboBox.getItems().add(allCourses);
        
        // Set converter to display course name
        courseFilterComboBox.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                if (course == null) return "";
                if (course.getCourseId() == -1) return "All Courses";
                return course.getCourseName() + " (" + course.getCourseCode() + ")";
            }
            
            @Override
            public Course fromString(String string) {
                return null; // Not used for ComboBox
            }
        });
        
        // Load instructor's courses
        try {
            List<Course> courses = courseService.getCoursesByInstructor(instructor.getUserId());
            if (courses != null && !courses.isEmpty()) {
                courseFilterComboBox.getItems().addAll(courses);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Default to All Courses
        courseFilterComboBox.setValue(allCourses);
        
        Label sortLabel = new Label("Sort:");
        sortLabel.setStyle("-fx-font-weight: bold;");
        
        ComboBox<String> sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("Name (A-Z)", "Name (Z-A)");
        sortComboBox.setValue("Name (A-Z)");
        
        filterBox.getChildren().addAll(courseFilterLabel, courseFilterComboBox, sortLabel, sortComboBox);
        
        // Students TableView
        TableView<Student> studentsTable = new TableView<>();
        studentsTable.setPlaceholder(new Label("No students found"));
        studentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Define columns
        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getFirstName() + " " + data.getValue().getLastName()));
        
        TableColumn<Student, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getEmail()));
        
        TableColumn<Student, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getStudentId()));
        
        TableColumn<Student, String> coursesCol = new TableColumn<>("Enrolled Courses");
        coursesCol.setCellValueFactory(data -> {
            try {
                List<Course> studentCourses = courseService.getCoursesByStudent(data.getValue().getUserId());
                if (studentCourses != null && !studentCourses.isEmpty()) {
                    return new SimpleStringProperty(
                            String.valueOf(studentCourses.size()));
                } else {
                    return new SimpleStringProperty("0");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new SimpleStringProperty("Error");
            }
        });
        
        // Add columns to table
        studentsTable.getColumns().addAll(nameCol, emailCol, idCol, coursesCol);
        
        // Make table fill available height
        VBox.setVgrow(studentsTable, Priority.ALWAYS);
        
        // Add double-click handler to view student details
        studentsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
                if (selectedStudent != null) {
                    showStudentResultDetails(selectedStudent);
                }
            }
        });
        
        // Function to update the table data
        Runnable updateTableData = () -> {
            try {
                studentsTable.getItems().clear();
                
                Course selectedCourse = courseFilterComboBox.getValue();
                String sortOrder = sortComboBox.getValue();
                boolean isAscending = sortOrder.contains("A-Z");
                
                List<Student> students;
                if (selectedCourse.getCourseId() == -1) {
                    // Get all students from all instructor's courses
                    List<Course> instructorCourses = courseService.getCoursesByInstructor(instructor.getUserId());
                    Set<Student> uniqueStudents = new HashSet<>();
                    
                    for (Course course : instructorCourses) {
                        List<Student> courseStudents = userService.getStudentsByCourse(course.getCourseId());
                        if (courseStudents != null) {
                            uniqueStudents.addAll(courseStudents);
                        }
                    }
                    
                    // Convert to list for sorting
                    students = new ArrayList<>(uniqueStudents);
                } else {
                    // Get students for selected course
                    students = userService.getStudentsByCourse(selectedCourse.getCourseId());
                }
                
                // Sort students
                if (students != null && !students.isEmpty()) {
                    if (isAscending) {
                        students.sort(Comparator.comparing(Student::getLastName)
                                .thenComparing(Student::getFirstName));
                    } else {
                        students.sort(Comparator.comparing(Student::getLastName)
                                .thenComparing(Student::getFirstName)
                                .reversed());
                    }
                    
                    studentsTable.getItems().addAll(students);
                }
            } catch (Exception e) {
                UIUtils.showErrorAlert("Error", "Failed to load students: " + e.getMessage());
                e.printStackTrace();
            }
        };
        
        // Run initial update
        updateTableData.run();
        
        // Add listeners for filter changes
        courseFilterComboBox.setOnAction(e -> updateTableData.run());
        sortComboBox.setOnAction(e -> updateTableData.run());
        
        // Add all components to the content
        content.getChildren().addAll(headerLabel, separator, filterBox, studentsTable);
        
        return content;
    }
    
    private void showStudentResultDetails(Student student) {
        try {
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(dashboard.getPrimaryStage());
            dialogStage.setTitle("Results for " + student.getFirstName() + " " + student.getLastName());
            dialogStage.setMinWidth(800);
            dialogStage.setMinHeight(500);
            
            // Create content layout
            VBox content = new VBox(15);
            content.setPadding(new Insets(20));
            content.setStyle("-fx-background-color: white;");
            
            // Student info header
            Label studentNameLabel = new Label(student.getFirstName() + " " + student.getLastName());
            studentNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            
            Label studentEmailLabel = new Label(student.getEmail());
            studentEmailLabel.setStyle("-fx-font-size: 14px;");
            
            // Divider
            Separator separator = new Separator();
            separator.setStyle("-fx-background-color: #e0e0e0;");
            
            // Get courses this student is enrolled in that are taught by this instructor
            List<Course> instructorCourses = courseService.getCoursesByInstructor(instructor.getUserId());
            List<Course> studentCourses = courseService.getCoursesByStudent(student.getUserId());
            
            // Filter to get only courses that match both lists
            List<Course> relevantCourses = instructorCourses.stream()
                    .filter(c -> studentCourses.stream().anyMatch(sc -> sc.getCourseId() == c.getCourseId()))
                    .collect(Collectors.toList());
            
            TabPane coursesTabPane = new TabPane();
            coursesTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            
            if (relevantCourses.isEmpty()) {
                Label noCourseLabel = new Label("This student is not enrolled in any of your courses.");
                noCourseLabel.setStyle("-fx-font-style: italic;");
                content.getChildren().add(noCourseLabel);
            } else {
                // Create a tab for each course
                for (Course course : relevantCourses) {
                    Tab courseTab = new Tab(course.getCourseName());
                    
                    // Create a VBox for the course content
                    VBox courseContent = new VBox(15);
                    courseContent.setPadding(new Insets(15));
                    
                    // Create a table view for exam results
                    TableView<Exam> examResultsTable = new TableView<>();
                    examResultsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    
                    // Define columns
                    TableColumn<Exam, String> examNameCol = new TableColumn<>("Exam");
                    examNameCol.setCellValueFactory(data -> new SimpleStringProperty(
                            data.getValue().getTitle()));
                    
                    TableColumn<Exam, String> statusCol = new TableColumn<>("Status");
                    statusCol.setCellValueFactory(data -> {
                        try {
                            boolean completed = examService.hasStudentCompletedExam(
                                    student.getUserId(), data.getValue().getExamId());
                            if (completed) {
                                return new SimpleStringProperty("Completed");
                            } else {
                                // Check if past due date
                                if (data.getValue().getDeadline() != null && 
                                    data.getValue().getDeadline().isBefore(LocalDateTime.now())) {
                                    return new SimpleStringProperty("Not Taken (Past Due)");
                                } else {
                                    return new SimpleStringProperty("Not Taken");
                                }
                            }
                        } catch (Exception e) {
                            return new SimpleStringProperty("Error");
                        }
                    });
                    
                    TableColumn<Exam, String> scoreCol = new TableColumn<>("Score");
                    scoreCol.setCellValueFactory(data -> {
                        try {
                            Report report = examService.getStudentReport(
                                    student.getUserId(), data.getValue().getExamId());
                            if (report != null && "completed".equals(report.getStatus())) {
                                return new SimpleStringProperty(
                                        report.getTotalScore() + "/" + report.getMaxScore());
                            } else {
                                return new SimpleStringProperty("-");
                            }
                        } catch (Exception e) {
                            return new SimpleStringProperty("Error");
                        }
                    });
                    
                    TableColumn<Exam, String> gradeCol = new TableColumn<>("Grade");
                    gradeCol.setCellValueFactory(data -> {
                        try {
                            Report report = examService.getStudentReport(
                                    student.getUserId(), data.getValue().getExamId());
                            if (report != null && "completed".equals(report.getStatus()) && report.getMaxScore() > 0) {
                                // Calculate percentage
                                double percentage = (double) report.getTotalScore() / report.getMaxScore() * 100;
                                return new SimpleStringProperty(String.format("%.1f%%", percentage));
                            } else {
                                return new SimpleStringProperty("-");
                            }
                        } catch (Exception e) {
                            return new SimpleStringProperty("Error");
                        }
                    });
                    
                    // Add columns to table
                    examResultsTable.getColumns().addAll(examNameCol, statusCol, scoreCol, gradeCol);
                    
                    // Add exams to table
                    try {
                        List<Exam> exams = examService.getExamsByCourse(course.getCourseId());
                        if (exams != null && !exams.isEmpty()) {
                            examResultsTable.getItems().addAll(exams);
                            
                            // Calculate and display overall course grade
                            double totalPoints = 0;
                            double earnedPoints = 0;
                            boolean hasCompletedExams = false;
                            
                            for (Exam exam : exams) {
                                Report report = examService.getStudentReport(
                                        student.getUserId(), exam.getExamId());
                                if (report != null && "completed".equals(report.getStatus())) {
                                    totalPoints += report.getMaxScore();
                                    earnedPoints += report.getTotalScore();
                                    hasCompletedExams = true;
                                }
                            }
                            
                            // Display overall grade if student has completed exams
                            if (hasCompletedExams && totalPoints > 0) {
                                double overallPercentage = (earnedPoints / totalPoints) * 100;
                                
                                HBox overallGradeBox = new HBox(10);
                                overallGradeBox.setAlignment(Pos.CENTER_RIGHT);
                                
                                Label overallLabel = new Label("Overall Course Grade:");
                                overallLabel.setStyle("-fx-font-weight: bold;");
                                
                                Label gradeLabel = new Label(String.format("%.1f%%", overallPercentage));
                                gradeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                                
                                overallGradeBox.getChildren().addAll(overallLabel, gradeLabel);
                                courseContent.getChildren().addAll(examResultsTable, overallGradeBox);
                            } else {
                                courseContent.getChildren().add(examResultsTable);
                            }
                        } else {
                            examResultsTable.setPlaceholder(new Label("No exams found for this course"));
                            courseContent.getChildren().add(examResultsTable);
                        }
                    } catch (Exception e) {
                        examResultsTable.setPlaceholder(new Label("Error loading exams: " + e.getMessage()));
                        courseContent.getChildren().add(examResultsTable);
                    }
                    
                    courseTab.setContent(courseContent);
                    coursesTabPane.getTabs().add(courseTab);
                }
                
                // Add the TabPane to the content
                content.getChildren().add(coursesTabPane);
            }
            
            // Close button
            Button closeButton = new Button("Close");
            closeButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            closeButton.setOnAction(e -> dialogStage.close());
            
            HBox buttonBox = new HBox(closeButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            
            // Add all components to the content
            content.getChildren().addAll(studentNameLabel, studentEmailLabel, separator, buttonBox);
            
            // Create scene and set to dialog stage
            Scene dialogScene = new Scene(content);
            dialogStage.setScene(dialogScene);
            
            // Show the dialog
            dialogStage.showAndWait();
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Failed to load student details: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 