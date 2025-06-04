@echo off
echo Running PaNotify Application

REM Set JavaFX path to the user's installation
set JAVAFX_PATH=D:\School\JAVA\javafx-sdk-24.0.1\lib
echo Using JavaFX from: %JAVAFX_PATH%

REM Check if JDBC driver exists
if not exist "lib\mssql-jdbc.jar" (
    echo JDBC driver not found. Please run build.bat first or download it manually.
    pause
    exit /b 1
)

REM Run the application with all necessary JavaFX modules
java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.media,javafx.web,javafx.swing -cp "bin;lib\*;%JAVAFX_PATH%\*" com.panotify.Main

echo Application exited
pause 