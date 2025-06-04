# PaNotify Application

An educational application for creating and managing online examinations.

## Setup Requirements

1. Java Development Kit (JDK) version 17 or higher
2. JavaFX SDK 24.0.1 or newer
3. A Microsoft SQL Server database
4. SQL Server JDBC driver (mssql-jdbc.jar)

## Configuration

The application is configured to use JavaFX from: D:\School\JAVA\javafx-sdk-24.0.1\lib

If you need to change this path:

1. Edit the `compile.bat` file:
   ```
   set JAVAFX_PATH=D:\School\JAVA\javafx-sdk-24.0.1\lib
   ```

2. Edit the `run.bat` file:
   ```
   set JAVAFX_PATH=D:\School\JAVA\javafx-sdk-24.0.1\lib
   ```

3. Database configuration is in `src/com/panotify/util/DatabaseUtil.java`:
   - Update the `DB_URL`, `DB_USER`, and `DB_PASSWORD` as needed

## Building and Running

1. To compile the application, run:
   ```
   compile.bat
   ```

2. To run the application, run:
   ```
   run.bat
   ```

## Project Structure

- `src/com/panotify/` - Main application package
  - `Main.java` - Application entry point
- `src/com/panotify/model/` - Data models (User, Student, Instructor, Course, Exam, etc.)
- `src/com/panotify/service/` - Business logic and database services
  - `AuthenticationService.java` - Handles user authentication
  - `CourseService.java` - Manages course-related operations
  - `ExamService.java` - Manages exam-related operations
  - `UserService.java` - Manages user-related operations
- `src/com/panotify/ui/` - User interface components
  - `LoginView.java` - Login screen for students and instructors
  - `UIUtils.java` - Common UI utilities
  - `student/` - Student-specific UI components
    - `StudentDashboard.java` - Main dashboard for students
  - `instructor/` - Instructor-specific UI components
    - `InstructorDashboard.java` - Main dashboard for instructors
- `src/com/panotify/util/` - Utility classes
  - `DatabaseUtil.java` - Database connection utilities
- `src/com/images/` - Application images and resources
  - `icon.png` - Application icon
  - `bg_page.png` - Main background image
  - `bg_2.jpg` - Secondary background image

## Eclipse Setup

1. In Eclipse, go to Window > Preferences > Java > Build Path > User Libraries
2. Create a "JavaFX" user library and add the JavaFX JAR files from D:\School\JAVA\javafx-sdk-24.0.1\lib
3. Create a "JDBC" user library and add the SQL Server JDBC driver (mssql-jdbc.jar)
4. Add the JavaFX VM arguments to your run configuration:
   ```
   --module-path "D:\School\JAVA\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media,javafx.web,javafx.swing
   ```

## Troubleshooting

If you encounter JavaFX-related errors:
1. Ensure your JavaFX SDK path is correctly set in both `compile.bat` and `run.bat`
2. Verify that all required JavaFX modules are included in the `--add-modules` argument

For database connection issues:
1. Verify that SQL Server is running and accessible
2. Check the connection details in `DatabaseUtil.java`
3. Ensure the JDBC driver is in the `lib` directory 