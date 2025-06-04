package com.panotify.ui.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.text.TextAlignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import com.panotify.model.Course;
import com.panotify.model.Exam;
import com.panotify.model.ExamResult;
import com.panotify.model.Student;
import com.panotify.model.Question;
import com.panotify.model.StudentAnswer;
import com.panotify.service.CourseService;
import com.panotify.service.ExamService;
import com.panotify.ui.UIUtils;

/**
 * Tab for displaying and taking exams
 * Provides interface for viewing available exams, filtering by course or status,
 * taking exams, and viewing completed exam results
 */
public class ExamsTab implements StudentTab {
    private Student student;
    private CourseService courseService;
    private ExamService examService;
    private StudentDashboard dashboard;
    private VBox content;
    
    // Filters
    private ComboBox<String> statusFilter;
    private ComboBox<Course> courseFilter;
    
    /**
     * Creates a new ExamsTab with the necessary services and data
     * 
     * @param student the logged-in student user
     * @param courseService service for course-related operations
     * @param examService service for exam-related operations
     * @param dashboard reference to the parent dashboard
     */
    public ExamsTab(Student student, CourseService courseService, ExamService examService, StudentDashboard dashboard) {
        this.student = student;
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
     * Refreshes the content and rebuilds the exams view
     */
    @Override
    public void onNavigatedTo() {
        content.getChildren().clear();
        content.getChildren().add(createExamsContent());
    }
    
    /**
     * Filters exams by a specific course
     * Updates the course filter dropdown and refreshes the exam list
     * 
     * @param course the course to filter by
     */
    public void filterByCourse(Course course) {
        if (courseFilter != null && course != null) {
            // Find the course in the filter items
            for (Course c : courseFilter.getItems()) {
                if (c.getCourseId() == course.getCourseId()) {
                    courseFilter.setValue(c);
                    refreshExams();
                    return;
                }
            }
            
            // If not found, add it to the items
            courseFilter.getItems().add(course);
            courseFilter.setValue(course);
            refreshExams();
        }
    }
    
    /**
     * Creates the exams content view
     * Sets up the UI with filters and exam list
     * 
     * @return a VBox containing the exams content view
     */
    private VBox createExamsContent() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        // Header
        Label headerLabel = new Label("My Exams");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Filter section
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        
        // Status filter
        Label statusLabel = new Label("Status:");
        statusLabel.setStyle("-fx-font-weight: bold;");
        
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Exams", "Upcoming", "Completed");
        statusFilter.setValue("All Exams");
        statusFilter.setOnAction(e -> refreshExams());
        
        // Course filter
        Label courseLabel = new Label("Course:");
        courseLabel.setStyle("-fx-font-weight: bold;");
        
        courseFilter = new ComboBox<>();
        
        // Add "All Courses" option
        Course allCourses = new Course();
        allCourses.setCourseId(-1);
        allCourses.setCourseName("All Courses");
        courseFilter.getItems().add(allCourses);
        
        try {
            List<Course> courses = courseService.getEnrolledCourses(student.getUserId());
            if (courses != null && !courses.isEmpty()) {
                courseFilter.getItems().addAll(courses);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Set default selection
        courseFilter.setValue(allCourses);
        
        // Set custom cell factory for course names
        courseFilter.setCellFactory(param -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getCourseName());
                }
            }
        });
        
        // Set custom string converter for display
        courseFilter.setConverter(new javafx.util.StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                if (course == null) return null;
                return course.getCourseName();
            }
            
            @Override
            public Course fromString(String string) {
                return null; // Not needed
            }
        });
        
        courseFilter.setOnAction(e -> refreshExams());
        
        // Add filters to filter box
        filterBox.getChildren().addAll(statusLabel, statusFilter, courseLabel, courseFilter);
        
        // Exams container
        VBox examsContainer = new VBox(15);
        examsContainer.setPadding(new Insets(10, 0, 0, 0));
        
        // Add components to container
        container.getChildren().addAll(headerLabel, filterBox, examsContainer);
        
        // Load exams
        loadExams(examsContainer);
        
        return container;
    }
    
    /**
     * Refreshes the exam list based on current filter selections
     * Finds the exams container and reloads the exam data
     */
    private void refreshExams() {
        VBox examsContainer = findExamsContainer();
        if (examsContainer != null) {
            loadExams(examsContainer);
        }
    }
    
    /**
     * Finds the exams container within the tab content hierarchy
     * 
     * @return the VBox containing the exam cards, or null if not found
     */
    private VBox findExamsContainer() {
        // Find the exams container in the content hierarchy
        for (var node : content.getChildren()) {
            if (node instanceof VBox) {
                VBox vbox = (VBox) node;
                for (var child : vbox.getChildren()) {
                    if (child instanceof VBox && vbox.getChildren().indexOf(child) == 2) {
                        return (VBox) child;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Loads and displays exams based on the current filter selections
     * Creates exam cards for each available exam
     * 
     * @param container the VBox to add exam cards to
     */
    private void loadExams(VBox container) {
        // Clear existing exams
        container.getChildren().clear();
        
        try {
            // Get filter values
            String status = statusFilter.getValue();
            Course course = courseFilter.getValue();
            int courseId = course != null ? course.getCourseId() : -1;
            
            // Get exams based on filters
            List<Exam> exams = examService.getExamsForStudent(student.getUserId());
            
            if (exams != null && !exams.isEmpty()) {
                for (Exam exam : exams) {
                    // Apply course filter
                    if (courseId != -1 && exam.getCourseId() != courseId) {
                        continue;
                    }
                    
                    // Apply status filter
                    boolean completed = examService.hasStudentCompletedExam(student.getUserId(), exam.getExamId());
                    
                    if ("Upcoming".equals(status) && completed) {
                        continue;
                    } else if ("Completed".equals(status) && !completed) {
                        continue;
                    }
                    
                    // Add exam card
                    container.getChildren().add(createExamCard(exam, completed));
                }
                
                if (container.getChildren().isEmpty()) {
                    showNoExamsMessage(container, "No exams match the selected filters");
                }
            } else {
                showNoExamsMessage(container, "No exams available");
            }
        } catch (Exception e) {
            Label errorLabel = new Label("Error loading exams: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
            container.getChildren().add(errorLabel);
            e.printStackTrace();
        }
    }
    
    /**
     * Shows a message when no exams are available
     * Displays an explanatory message and icon
     * 
     * @param container the VBox to add the message to
     * @param message the message to display
     */
    private void showNoExamsMessage(VBox container, String message) {
        VBox messageBox = new VBox(10);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(30));
        
        Label noExamsLabel = new Label(message);
        noExamsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d;");
        
        messageBox.getChildren().add(noExamsLabel);
        container.getChildren().add(messageBox);
    }
    
    /**
     * Creates an exam card UI component for a specific exam
     * Displays exam information and provides action buttons
     * 
     * @param exam the exam to create a card for
     * @param completed whether the student has completed this exam
     * @return an HBox containing the styled exam card
     */
    private HBox createExamCard(Exam exam, boolean completed) {
        HBox card = new HBox();
        card.setPadding(new Insets(15));
        card.setSpacing(20);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5; -fx-border-color: #e9ecef; -fx-border-radius: 5;");
        
        // Exam info
        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(300);
        
        Label titleLabel = new Label(exam.getTitle());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Get course name
        String courseName = "Unknown Course";
        try {
            Course course = courseService.getCourseById(exam.getCourseId());
            if (course != null) {
                courseName = course.getCourseName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Label courseLabel = new Label("Course: " + courseName);
        courseLabel.setStyle("-fx-font-size: 14px;");
        
        Label durationLabel = new Label("Duration: " + exam.getDurationMinutes() + " minutes");
        durationLabel.setStyle("-fx-font-size: 14px;");
        
        // Format deadline if available
        if (exam.getDeadline() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String deadline = exam.getDeadline().format(formatter);
            Label deadlineLabel = new Label("Deadline: " + deadline);
            deadlineLabel.setStyle("-fx-font-size: 14px;");
            infoBox.getChildren().addAll(titleLabel, courseLabel, durationLabel, deadlineLabel);
        } else {
            infoBox.getChildren().addAll(titleLabel, courseLabel, durationLabel);
        }
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Status and action button
        VBox actionsBox = new VBox(10);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);
        
        Label statusLabel;
        Button actionButton;
        
        if (completed) {
            statusLabel = new Label("Status: Completed");
            statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #28a745; -fx-font-weight: bold;");
            
            actionButton = new Button("View Results");
            actionButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");
            actionButton.setOnAction(e -> viewExamResults(exam));
        } else {
            statusLabel = new Label("Status: Not Taken");
            statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #dc3545; -fx-font-weight: bold;");
            
            actionButton = new Button("Take Exam");
            actionButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
            actionButton.setOnAction(e -> takeExam(exam));
            
            // Check if exam has a deadline and it's passed
            if (exam.getDeadline() != null && exam.getDeadline().isBefore(LocalDateTime.now())) {
                actionButton.setDisable(true);
                statusLabel.setText("Status: Deadline Passed");
            }
        }
        
        actionButton.setPrefWidth(120);
        
        // Add info and button to actions box
        actionsBox.getChildren().addAll(statusLabel, actionButton);
        
        // Add components to card
        card.getChildren().addAll(infoBox, spacer, actionsBox);
        
        return card;
    }
    
    /**
     * Starts the process of taking an exam
     * Checks if the exam can be taken and shows the exam interface
     * 
     * @param exam the exam to take
     */
    private void takeExam(Exam exam) {
        try {
            // Check if exam is already completed
            boolean completed = examService.hasStudentCompletedExam(student.getUserId(), exam.getExamId());
            
            if (completed) {
                UIUtils.showInfoAlert("Already Submitted", 
                    "You have already submitted this exam. You cannot take it again.");
                return;
            }
            
            // Check if exam is published
            if (!exam.isPublished()) {
                UIUtils.showInfoAlert("Exam Not Available", 
                    "This exam is not yet available for submission.");
                return;
            }
            
            // Check if the deadline has passed
            if (exam.getDeadline() != null && exam.getDeadline().isBefore(LocalDateTime.now())) {
                UIUtils.showInfoAlert("Deadline Passed", 
                    "The deadline for this exam has already passed.");
                return;
            }
            
            // Confirm taking the exam
            boolean confirm = UIUtils.showConfirmationAlert(
                "Start Exam", 
                "Are you sure you want to start this exam? Once started, the timer will begin and you must complete the exam.\n\n" +
                "Exam: " + exam.getTitle() + "\n" +
                "Duration: " + exam.getDurationMinutes() + " minutes"
            );
            
            if (confirm) {
                // Use the new ExamPanel instead of a dialog
                content.getChildren().clear();
                ExamPanel examPanel = new ExamPanel(this, exam, examService, student.getUserId());
                content.getChildren().add(examPanel.createContent());
            }
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not start exam: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Displays the exam in the main content panel
     * Creates and shows the ExamPanel for taking the exam
     * 
     * @param exam the exam to display
     */
    private void showExamInMainPanel(Exam exam) {
        try {
            // Get the actual questions for this exam
            List<Question> questions = examService.getExamQuestions(exam.getExamId());
            
            if (questions == null || questions.isEmpty()) {
                UIUtils.showErrorAlert("Error", "This exam has no questions. Please contact your instructor.");
                return;
            }
            
            // Clear the main content
            content.getChildren().clear();
            
            // Create main container for the exam
            VBox examContainer = new VBox(20);
            examContainer.setPadding(new Insets(20));
            examContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
            
            // Header with exam title and time
            HBox headerBox = new HBox(15);
            headerBox.setAlignment(Pos.CENTER_LEFT);
            
            Label titleLabel = new Label("Exam: " + exam.getTitle());
            titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            
            Label timeLabel = new Label("Time Remaining: " + exam.getDurationMinutes() + " minutes");
            timeLabel.setStyle("-fx-font-size: 16px;");
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            Button backButton = new Button("Back to Exams");
            backButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
            backButton.setOnAction(e -> {
                boolean confirmExit = UIUtils.showConfirmationDialog(
                    "Exit Exam", 
                    "Are you sure you want to exit this exam? Your answers will not be saved.",
                    "Exit Exam", "Continue Exam"
                );
                
                if (confirmExit) {
                    onNavigatedTo(); // Return to the exams list
                }
            });
            
            headerBox.getChildren().addAll(titleLabel, spacer, timeLabel, backButton);
            
            // Instructions
            Label instructionsLabel = new Label("Answer all questions and click 'Submit Exam' when done.");
            instructionsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            instructionsLabel.setWrapText(true);
            
            // Scrollable container for questions
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            
            VBox questionsBox = new VBox(20);
            questionsBox.setPadding(new Insets(20));
            
            // Map to store question IDs and responses
            Map<Integer, Object> answers = new HashMap<>();
            
            // Create a panel for each question
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                
                VBox questionBox = new VBox(10);
                questionBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-background-radius: 5; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
                
                Label questionLabel = new Label((i + 1) + ". " + question.getQuestionText());
                questionLabel.setWrapText(true);
                questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                
                questionBox.getChildren().add(questionLabel);
                
                // Add points info
                Label pointsLabel = new Label("(" + question.getPoints() + " points)");
                pointsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #6c757d;");
                questionBox.getChildren().add(pointsLabel);
                
                // Process options
                if (question.getOptions() != null && question.getOptions().contains("|")) {
                    // Multiple choice question
                    ToggleGroup optionsGroup = new ToggleGroup();
                    String[] options = question.getOptions().split("\\|");
                    
                    for (int j = 0; j < options.length; j++) {
                        RadioButton option = new RadioButton(options[j]);
                        option.setToggleGroup(optionsGroup);
                        option.setWrapText(true);
                        option.setUserData(j); // Store option index
                        questionBox.getChildren().add(option);
                        
                        // Add change listener to store answer
                        final int optionIndex = j;
                        option.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            if (newVal) {
                                answers.put(question.getQuestionId(), optionIndex);
                            }
                        });
                    }
                } else {
                    // Identification question
                    Label answerInstructionLabel = new Label("Type your answer below:");
                    answerInstructionLabel.setStyle("-fx-font-style: italic;");
                    
                    TextField answerField = new TextField();
                    answerField.setPromptText("Enter your answer here");
                    answerField.setPrefWidth(600);
                    
                    // Add listener to store answer
                    answerField.textProperty().addListener((obs, oldVal, newVal) -> {
                        answers.put(question.getQuestionId(), newVal.trim());
                    });
                    
                    questionBox.getChildren().addAll(answerInstructionLabel, answerField);
                }
                
                questionsBox.getChildren().add(questionBox);
            }
            
            scrollPane.setContent(questionsBox);
            scrollPane.setPrefHeight(400);
            
            // Submit button area
            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            buttonBox.setPadding(new Insets(10, 0, 0, 0));
            
            Button submitButton = new Button("Submit Exam");
            submitButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px;");
            submitButton.setPrefWidth(150);
            submitButton.setPrefHeight(40);
            
            submitButton.setOnAction(e -> {
                // Show confirmation dialog
                String message = "Are you sure you want to submit this exam?\n" +
                                "Once submitted, you cannot make changes.\n\n" +
                                "Exam: " + exam.getTitle() + "\n" +
                                "Questions Answered: " + answers.size() + "/" + questions.size();
                
                boolean confirm = UIUtils.showConfirmationDialog(
                    "Confirm Submission",
                    message,
                    "Submit Exam",
                    "Review Answers"
                );
                
                if (confirm) {
                    // Convert answers to the expected format
                    Map<Integer, String> formattedAnswers = new HashMap<>();
                    for (Map.Entry<Integer, Object> entry : answers.entrySet()) {
                        if (entry.getValue() instanceof Integer) {
                            formattedAnswers.put(entry.getKey(), entry.getValue().toString());
                        } else if (entry.getValue() instanceof String) {
                            formattedAnswers.put(entry.getKey(), (String) entry.getValue());
                        }
                    }
                    
                    // Submit the exam
                    submitExamAnswers(exam, formattedAnswers, questions);
                    
                    // Return to the exams list
                    onNavigatedTo();
                }
                // If not confirmed, just stay on the exam page
            });
            
            buttonBox.getChildren().add(submitButton);
            
            // Add all components to the exam container
            examContainer.getChildren().addAll(headerBox, instructionsLabel, scrollPane, buttonBox);
            
            // Set the exam container as the main content
            content.getChildren().add(examContainer);
                
                // Start the exam in the database
                boolean examStarted = examService.startExam(student.getUserId(), exam.getExamId());
                if (!examStarted) {
                    UIUtils.showErrorAlert("Error", "Failed to record exam start. Please try again.");
                onNavigatedTo(); // Return to exams list
                    return;
                }
                
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not start exam: " + e.getMessage());
            e.printStackTrace();
            onNavigatedTo(); // Return to exams list
        }
    }
    
    /**
     * Submits the student's answers for an exam
     * Sends answers to the server and processes the result
     * 
     * @param exam the exam being submitted
     * @param answers map of question IDs to student answers
     * @param questions list of questions in the exam
     */
    private void submitExamAnswers(Exam exam, Map<Integer, String> answers, List<Question> questions) {
        try {
                // Submit each answer
                boolean allAnswersSubmitted = true;
                for (Question question : questions) {
                    String answer = answers.getOrDefault(question.getQuestionId(), "");
                    
                    // Check if answer is empty
                    if (answer.isEmpty()) {
                        allAnswersSubmitted = false;
                        continue;
                    }
                    
                    // Determine if the answer is correct
                    boolean isCorrect = false;
                    if (question.getOptions() != null && question.getOptions().contains("|")) {
                        // Multiple choice
                        try {
                            int selectedOption = Integer.parseInt(answer);
                            isCorrect = (selectedOption == question.getCorrectOption());
                        } catch (NumberFormatException e) {
                            // Invalid answer format
                            isCorrect = false;
                        }
                    } else {
                        // Identification - case insensitive match
                        String correctAnswer = question.getCorrectAnswer().trim();
                        String studentAnswer = answer.trim();
                        isCorrect = studentAnswer.equalsIgnoreCase(correctAnswer);
                        System.out.println("Identification question: student answer='" + studentAnswer + 
                                          "', correct answer='" + correctAnswer + "', isCorrect=" + isCorrect);
                    }
                    
                    // Create and submit the answer
                    StudentAnswer studentAnswer = new StudentAnswer(
                        student.getUserId(),
                        question.getQuestionId(),
                        answer
                    );
                    studentAnswer.setCorrect(isCorrect);
                    studentAnswer.setSubmittedAt(LocalDateTime.now());
                    
                    boolean answerSubmitted = examService.submitAnswer(studentAnswer);
                    if (!answerSubmitted) {
                        allAnswersSubmitted = false;
                    }
                }
                
                // Complete the exam
                boolean examCompleted = examService.submitExam(student.getUserId(), exam.getExamId());
                
                if (examCompleted && allAnswersSubmitted) {
                    // Grade the exam
                    boolean graded = examService.gradeExam(student.getUserId(), exam.getExamId());
                    
                    if (graded) {
                        // If there's no deadline, show results immediately
                        if (exam.getDeadline() == null) {
                            // Get the exam result to display score
                            ExamResult examResult = examService.getExamResult(student.getUserId(), exam.getExamId());
                            if (examResult != null) {
                                double percentage = 0;
                                if (examResult.getMaxScore() > 0) {
                                    percentage = ((double) examResult.getTotalScore() / examResult.getMaxScore()) * 100;
                                }
                                
                                UIUtils.showSuccessAlert(
                                    "Exam Submitted", 
                                    String.format("Your exam has been submitted and graded successfully!\n\nScore: %d/%d (%.1f%%)", 
                                        examResult.getTotalScore(), 
                                        examResult.getMaxScore(),
                                        percentage)
                                );
                                
                                // Show detailed results
                                viewExamResults(exam);
                            } else {
                                UIUtils.showSuccessAlert("Exam Submitted", "Your exam has been submitted and graded successfully!");
                            }
                        } else {
                            UIUtils.showSuccessAlert("Exam Submitted", "Your exam has been submitted and graded successfully!");
                        }
                    } else {
                        UIUtils.showSuccessAlert("Exam Submitted", "Your exam has been submitted successfully, but there was an issue with grading. Your instructor will be notified.");
                    }
                    
                    refreshExams(); // Refresh the exams list to update the status
                } else {
                    UIUtils.showErrorAlert("Error", "There was an issue submitting some of your answers. Please try again or contact your instructor.");
            }
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not submit exam: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Shows the results of a completed exam
     * Displays score, answers, and correct solutions
     * 
     * @param exam the exam to view results for
     */
    private void viewExamResults(Exam exam) {
        try {
            // Check if the exam has a deadline and it hasn't passed yet
            if (exam.getDeadline() != null && exam.getDeadline().isAfter(LocalDateTime.now())) {
                UIUtils.showInfoAlert("Results Not Available", 
                    "Exam results will be available after the deadline: " + 
                    exam.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                return;
            }
            
            // Navigate to the results tab
            dashboard.navigateToMyResults();
            
            // Filter to show this specific exam's results
            if (dashboard instanceof StudentDashboard) {
                ResultsTab resultsTab = ((StudentDashboard) dashboard).getResultsTab();
                if (resultsTab != null) {
                    resultsTab.filterByExam(exam.getExamId());
                }
            }
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not view exam results: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 