package com.panotify.ui.student;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.panotify.model.Course;
import com.panotify.model.ExamResult;
import com.panotify.model.QuestionResult;
import com.panotify.model.Question;
import com.panotify.model.Student;
import com.panotify.service.ExamService;
import com.panotify.ui.UIUtils;

/**
 * Tab for viewing student exam results
 * Provides interface for filtering results by course,
 * viewing overall results, and inspecting detailed exam answers
 */
public class ResultsTab implements StudentTab {
    private Student student;
    private ExamService examService;
    private StudentDashboard dashboard;
    private VBox content;
    
    // Filter
    private ComboBox<Course> courseFilter;
    
    /**
     * Creates a new ResultsTab with the necessary services and data
     * 
     * @param student the logged-in student user
     * @param examService service for exam-related operations
     * @param dashboard reference to the parent dashboard
     */
    public ResultsTab(Student student, ExamService examService, StudentDashboard dashboard) {
        this.student = student;
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
     * Refreshes the content and rebuilds the results view
     */
    @Override
    public void onNavigatedTo() {
        content.getChildren().clear();
        content.getChildren().add(createResultsContent());
    }
    
    /**
     * Creates the results content view
     * Sets up the UI with filters and results list
     * 
     * @return a VBox containing the results content view
     */
    private VBox createResultsContent() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        // Header
        Label headerLabel = new Label("My Results");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Filter section
        HBox filterBox = new HBox(15);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        
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
            // Get courses from service
            List<Course> courses = dashboard.getCourseService().getEnrolledCourses(student.getUserId());
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
        
        courseFilter.setOnAction(e -> refreshResults());
        
        // Add filters to filter box
        filterBox.getChildren().addAll(courseLabel, courseFilter);
        
        // Results container
        VBox resultsContainer = new VBox(15);
        resultsContainer.setPadding(new Insets(10, 0, 0, 0));
        
        // Add components to container
        container.getChildren().addAll(headerLabel, filterBox, resultsContainer);
        
        // Load results
        loadResults(resultsContainer);
        
        return container;
    }
    
    /**
     * Refreshes the results list based on current filter selections
     * Finds the results container and reloads the result data
     */
    private void refreshResults() {
        VBox resultsContainer = findResultsContainer();
        if (resultsContainer != null) {
            loadResults(resultsContainer);
        }
    }
    
    /**
     * Finds the results container within the tab content hierarchy
     * 
     * @return the VBox containing the result cards, or null if not found
     */
    private VBox findResultsContainer() {
        // Find the results container in the content hierarchy
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
     * Loads and displays exam results based on the current filter selections
     * Creates result cards for each completed exam
     * 
     * @param container the VBox to add result cards to
     */
    private void loadResults(VBox container) {
        // Clear existing results
        container.getChildren().clear();
        
        try {
            // Get filter values
            Course course = courseFilter.getValue();
            int courseId = course != null ? course.getCourseId() : -1;
            
            // Get results based on filters
            List<ExamResult> results = examService.getStudentResults(student.getUserId());
            
            if (results != null && !results.isEmpty()) {
                for (ExamResult result : results) {
                    // Apply course filter if needed
                    if (courseId != -1) {
                        // We need to get the course ID from the exam
                        try {
                            int examCourseId = examService.getExamById(result.getExamId()).getCourseId();
                            if (examCourseId != courseId) {
                                continue;
                            }
                        } catch (Exception e) {
                            continue; // Skip if we can't determine the course
                        }
                    }
                    
                    // Add result card
                    container.getChildren().add(createResultCard(result));
                }
                
                if (container.getChildren().isEmpty()) {
                    showNoResultsMessage(container, "No results match the selected filter");
                }
            } else {
                showNoResultsMessage(container, "No exam results available");
            }
        } catch (Exception e) {
            Label errorLabel = new Label("Error loading results: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
            container.getChildren().add(errorLabel);
            e.printStackTrace();
        }
    }
    
    /**
     * Shows a message when no results are available
     * Displays an explanatory message and icon
     * 
     * @param container the VBox to add the message to
     * @param message the message to display
     */
    private void showNoResultsMessage(VBox container, String message) {
        VBox messageBox = new VBox(10);
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(30));
        
        Label noResultsLabel = new Label(message);
        noResultsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d;");
        
        messageBox.getChildren().add(noResultsLabel);
        container.getChildren().add(messageBox);
    }
    
    /**
     * Creates a result card UI component for a specific exam result
     * Displays exam information, score, and grade
     * 
     * @param result the exam result to create a card for
     * @return an HBox containing the styled result card
     */
    private HBox createResultCard(ExamResult result) {
        HBox card = new HBox();
        card.setPadding(new Insets(15));
        card.setSpacing(20);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5; -fx-border-color: #e9ecef; -fx-border-radius: 5;");
        
        // Result info
        VBox infoBox = new VBox(5);
        infoBox.setPrefWidth(300);
        
        Label titleLabel = new Label(result.getExamTitle());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Format date if available
        String completionDate = "Not available";
        if (result.getSubmittedAt() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            completionDate = result.getSubmittedAt().format(formatter);
        }
        
        Label dateLabel = new Label("Completed: " + completionDate);
        dateLabel.setStyle("-fx-font-size: 14px;");
        
        // Calculate percentage score
        double percentage = 0;
        if (result.getMaxScore() > 0) {
            percentage = ((double) result.getTotalScore() / result.getMaxScore()) * 100;
        }
        
        Label scoreLabel = new Label(String.format("Score: %d/%d (%.1f%%)", 
                                                   result.getTotalScore(), 
                                                   result.getMaxScore(), 
                                                   percentage));
        scoreLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Color the score based on performance
        if (percentage >= 80) {
            scoreLabel.setTextFill(Color.GREEN);
        } else if (percentage >= 60) {
            scoreLabel.setTextFill(Color.ORANGE);
        } else {
            scoreLabel.setTextFill(Color.RED);
        }
        
        infoBox.getChildren().addAll(titleLabel, dateLabel, scoreLabel);
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Action button
        VBox actionsBox = new VBox(10);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);
        
        Button detailsBtn = new Button("View Details");
        detailsBtn.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");
        detailsBtn.setPrefWidth(120);
        detailsBtn.setOnAction(e -> showResultDetails(result));
        
        actionsBox.getChildren().add(detailsBtn);
        
        // Add components to card
        card.getChildren().addAll(infoBox, spacer, actionsBox);
        
        return card;
    }
    
    /**
     * Shows a dialog with detailed information about an exam result
     * Displays score, grade, and question-by-question breakdown
     * 
     * @param result the exam result to show details for
     */
    private void showResultDetails(ExamResult result) {
        try {
            // Fetch question results for this exam
            List<QuestionResult> questionResults = examService.getQuestionResults(student.getUserId(), result.getExamId());
            
            // Also fetch the original questions to get access to options for multiple choice
            List<Question> originalQuestions = examService.getExamQuestions(result.getExamId());
            
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Exam Result Details");
            dialog.setHeaderText("Results for: " + result.getExamTitle());
            
            // Create content pane
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(500);
            
            VBox content = new VBox(20);
            content.setPadding(new Insets(20));
            content.setPrefWidth(700);
            
            // Summary section
            VBox summaryBox = new VBox(10);
            summaryBox.setPadding(new Insets(15));
            summaryBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5;");
            
            // Calculate percentage score
            double percentage = 0;
            if (result.getMaxScore() > 0) {
                percentage = ((double) result.getTotalScore() / result.getMaxScore()) * 100;
            }
            
            Label summaryLabel = new Label(String.format("Final Score: %d/%d (%.1f%%)", 
                                                        result.getTotalScore(), 
                                                        result.getMaxScore(), 
                                                        percentage));
            summaryLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            
            // Format date if available
            String completionDate = "Not available";
            if (result.getSubmittedAt() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                completionDate = result.getSubmittedAt().format(formatter);
            }
            
            Label dateLabel = new Label("Completed on: " + completionDate);
            
            // Add grade based on percentage
            String gradeText = "Grade: ";
            if (percentage >= 90) {
                gradeText += "A (Excellent)";
            } else if (percentage >= 80) {
                gradeText += "B (Good)";
            } else if (percentage >= 70) {
                gradeText += "C (Satisfactory)";
            } else if (percentage >= 60) {
                gradeText += "D (Pass)";
            } else {
                gradeText += "F (Fail)";
            }
            
            Label gradeLabel = new Label(gradeText);
            gradeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            
            summaryBox.getChildren().addAll(summaryLabel, dateLabel, gradeLabel);
            
            // Add summary to content
            content.getChildren().add(summaryBox);
            
            // Add each question
            if (questionResults != null && !questionResults.isEmpty()) {
                for (int i = 0; i < questionResults.size(); i++) {
                    QuestionResult qr = questionResults.get(i);
                    
                    VBox questionBox = new VBox(10);
                    questionBox.setPadding(new Insets(15));
                    questionBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5; -fx-border-color: #dee2e6; -fx-border-radius: 5;");
                    
                    // Question header with points
                    HBox headerBox = new HBox(10);
                    headerBox.setAlignment(Pos.CENTER_LEFT);
                    
                    Label questionNumLabel = new Label("Question " + (i + 1));
                    questionNumLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                    
                    // Find the original question to get points
                    Question originalQuestion = null;
                    for (Question q : originalQuestions) {
                        if (q.getQuestionId() == qr.getQuestionId()) {
                            originalQuestion = q;
                            break;
                        }
                    }
                    
                    Label pointsLabel = new Label("(" + (originalQuestion != null ? originalQuestion.getPoints() : "?") + " points)");
                    pointsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #6c757d;");
                    
                    headerBox.getChildren().addAll(questionNumLabel, pointsLabel);
                    
                    // Question text
                    Label questionTextLabel = new Label(qr.getQuestionText());
                    questionTextLabel.setStyle("-fx-font-size: 14px;");
                    questionTextLabel.setWrapText(true);
                    
                    questionBox.getChildren().addAll(headerBox, questionTextLabel);
                    
                    // For multiple choice questions, show all options
                    if (originalQuestion != null && originalQuestion.getOptions() != null && !originalQuestion.getOptions().isEmpty()) {
                        String[] options = originalQuestion.getOptions().split("\\|");
                        VBox optionsBox = new VBox(5);
                        optionsBox.setPadding(new Insets(10, 0, 10, 20));
                        
                        // Get the student's answer as an index if possible
                        int studentAnswerIndex = -1;
                        try {
                            studentAnswerIndex = Integer.parseInt(qr.getStudentAnswer());
                        } catch (NumberFormatException e) {
                            // Not a number, leave as -1
                        }
                        
                        for (int j = 0; j < options.length; j++) {
                            HBox optionRow = new HBox(10);
                            
                            String optionText = options[j];
                            String prefix = j == originalQuestion.getCorrectOption() ? "✓ " : "   ";
                            
                            Label optionLabel = new Label(prefix + (char)('A' + j) + ". " + optionText);
                            optionLabel.setWrapText(true);
                            
                            // Style based on correctness
                            if (j == originalQuestion.getCorrectOption()) {
                                // Correct answer
                                optionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #28a745;");
                            } else if (j == studentAnswerIndex) {
                                // Incorrect student answer
                                optionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #dc3545;");
                            }
                            
                            optionRow.getChildren().add(optionLabel);
                            optionsBox.getChildren().add(optionRow);
                        }
                        
                        questionBox.getChildren().add(optionsBox);
                    } else {
                        // Free text question
                        // Your answer
                        Label yourAnswerLabel = new Label("Your Answer: " + qr.getStudentAnswer());
                        yourAnswerLabel.setStyle("-fx-font-size: 14px;");
                        yourAnswerLabel.setWrapText(true);
                        
                        // Correct answer
                        Label correctAnswerLabel = new Label("Correct Answer: " + qr.getCorrectAnswer());
                        correctAnswerLabel.setStyle("-fx-font-size: 14px;");
                        correctAnswerLabel.setWrapText(true);
                        
                        questionBox.getChildren().addAll(yourAnswerLabel, correctAnswerLabel);
                    }
                    
                    // Status
                    HBox resultRow = new HBox(10);
                    resultRow.setAlignment(Pos.CENTER_RIGHT);
                    
                    Label statusLabel = new Label(qr.isCorrect() ? "Correct" : "Incorrect");
                    statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5; -fx-background-radius: 3;");
                    
                    if (qr.isCorrect()) {
                        statusLabel.setStyle(statusLabel.getStyle() + "; -fx-background-color: #d4edda; -fx-text-fill: #155724;");
                    } else {
                        statusLabel.setStyle(statusLabel.getStyle() + "; -fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                    }
                    
                    resultRow.getChildren().add(statusLabel);
                    questionBox.getChildren().add(resultRow);
                    
                    content.getChildren().add(questionBox);
                }
            } else {
                Label noQuestionsLabel = new Label("No detailed question results available.");
                noQuestionsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6c757d;");
                content.getChildren().add(noQuestionsLabel);
            }
            
            scrollPane.setContent(content);
            dialog.getDialogPane().setContent(scrollPane);
            dialog.getDialogPane().setPrefWidth(750);
            
            // Add close button
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            
            dialog.showAndWait();
        } catch (Exception e) {
            UIUtils.showErrorAlert("Error", "Could not load exam details: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Filters results to show only results for a specific exam
     * Used when navigating directly from an exam to its results
     * 
     * @param examId the ID of the exam to filter by
     */
    public void filterByExam(int examId) {
        if (examId > 0) {
            VBox resultsContainer = findResultsContainer();
            if (resultsContainer != null) {
                // Clear existing results
                resultsContainer.getChildren().clear();
                
                try {
                    // Get the specific exam result
                    ExamResult result = examService.getExamResult(student.getUserId(), examId);
                    if (result != null) {
                        resultsContainer.getChildren().add(createResultCard(result));
                    } else {
                        showNoResultsMessage(resultsContainer, "No results available for this exam yet");
                    }
                } catch (Exception e) {
                    Label errorLabel = new Label("Error loading results: " + e.getMessage());
                    errorLabel.setStyle("-fx-text-fill: red;");
                    resultsContainer.getChildren().add(errorLabel);
                    e.printStackTrace();
                }
            }
        }
    }
} 